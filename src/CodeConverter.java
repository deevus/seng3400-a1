/**
 * SENG3400 Assignment 1
 * Simon Hartcher
 * C3185790
 */

import java.io.*;
import java.net.*;

/**
 * CodeConverter server class
 */
class CodeConverter {
    /**
     * CodeConverter entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {
        //init portnumber
        int portNumber = 0;

        //try to parse arguments
        try {
            //default to 1050 if not provided
            portNumber = args.length == 0 ? 1050 : Integer.parseInt(args[0]);

            //error if outside allowed port range
            if (portNumber < 1024) {
                System.err.println("Port number must be greater than 1023");
                System.exit(1);
            }
        } catch (Exception e ) {

            //if we hit an error, print the help text
            System.err.println("Usage: java CodeConverter <port number>");
            System.err.println("  <port number>: defaults to 1050");
            System.exit(1);

        }

        //print server info
        System.out.println("Hosting CodeConverter on port " + portNumber + "...");

        //keep accepting new connections until we have received an END message
        boolean ending = false;
        while (!ending) {
            ending = startServer(portNumber);
        }
    }

    /**
     * Starts the CodeConverter server
     * @param  portNumber The port number to host on
     * @return            A true value indicates that the server should close
     */
    static boolean startServer(int portNumber) {
        //init sockets and connections
        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
            new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
        ) {

            //init variables
            String inputLine, outputLine;

            // Initiate conversation with client
            CodeProtocol protocol = new CodeProtocol();

            //store the initial protocol state
            CodeProtocolState serverState = protocol.getState();

            //while we're receiving data from the client
            while ((inputLine = in.readLine()) != null) {
                //process the line
                outputLine = protocol.processInput(inputLine);

                //get the updated state
                serverState = protocol.getState();

                //print the output
                out.println(outputLine);
            }

            //if the client sent an end state, the server will close
            return serverState == CodeProtocolState.End;
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

        //there was an error, so we'll close the server
        return true;
    }
}
