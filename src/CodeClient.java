/**
 * SENG3400 Assignment 1
 * Simon Hartcher
 * C3185790
 */

import java.net.*;
import java.io.*;

/**
 * CodeClient server class
 */
class CodeClient {
    /**
     * Entry point for CodeClient
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        //init variables
        String hostName = null;
        int portNumber = 0;

        //parse arguments
        try {

            //default to localhost if not given
            hostName = args.length < 1 ? "localhost" : args[0];

            //default to 1050 if not given
            portNumber = args.length < 2 ? 1050 : Integer.parseInt(args[1]);

            //check if valid portnumber provided
            if (portNumber < 1024) {
                System.err.println("Port number must be greater than 1023");
                System.exit(1);
            }

        } catch (Exception e) {

            //print help text if we reach an exception state
            System.err.println("Usage: java CodeClient <host name> <port number>");
            System.err.println("    <host name>: defaults to localhost");
            System.err.println("  <port number>: defaults to 1050");
            System.exit(1);

        }

        //init connection
        try (
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        ) {
            //get input stream to process client commands
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));

            //init variables
            String fromUser;
            String fromServer = null;

            //handshake with the server
            if (!handShake(out, in)) {
                throw new UnknownHostException();
            }

            //loop until done
            boolean running = true;
            while (running) {
                //get command from client
                System.out.print("Enter command to send to server "
                    + "[AC,CA,BYE,END, or something to convert]: ");
                fromUser = stdIn.readLine();

                //if a command was provided
                if (fromUser != null) {
                    //print to stdout
                    System.out.println("Client: " + fromUser);

                    //send to server
                    out.println(fromUser);
                }

                //process response until we receive ASCII: OK
                while (!(fromServer = in.readLine()).equals("ASCII: OK")) {
                    System.out.println("Server: " + fromServer);

                    //if we see BYE or END we can end the client session
                    switch (fromServer) {
                        case "BYE: OK":
                        case "END: OK":
                            running = false;
                            break;
                    }

                    //break out of loop if the session is ending
                    if (!running)
                        break;
                }

                //this will be the ASCII: OK message
                //while the server is still accepting commands from us
                if (running)
                    System.out.println("Server: " + fromServer);
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
            hostName + " on port " + portNumber);
            System.exit(1);
        }
    }

    /**
     * Initiates a handshake with the CodeConverter server
     * @param   The server output stream
     * @param   The server input stream
     * @return  A true value indicates a valid handshake
     */
    private static boolean handShake(PrintWriter out, BufferedReader in)
        throws IOException {

        //the client handshake message
        String handShakeMessage = "ASCII";

        //print to stdout
        System.out.println("Client: " + handShakeMessage);

        //send to server
        out.println(handShakeMessage);

        //wait for response
        String response = null;
        while ((response = in.readLine()) == null) { }

        System.out.println("Server: " + response);

        //return whether we received valid response
        return response.equals("ASCII: OK");
    }
}
