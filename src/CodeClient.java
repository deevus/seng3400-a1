import java.net.*;
import java.io.*;

class CodeClient {
    public static void main(String[] args) {

        String hostName = null;
        int portNumber = 0;

        try {

            hostName = args.length < 1 ? "localhost" : args[0];
            portNumber = args.length < 2 ? 1050 : Integer.parseInt(args[1]);
            if (portNumber < 1024) {
                System.err.println("Port number must be greater than 1023");
                System.exit(1);
            }

        } catch (Exception e) {

            System.err.println("Usage: java CodeClient <host name> <port number>");
            System.err.println("    <host name>: defaults to localhost");
            System.err.println("  <port number>: defaults to 1050");
            System.exit(1);

        }

        try (
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        ) {
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromUser;

            if (!handShake(out, in)) {
                throw new UnknownHostException();
            }

            String fromServer = null;
            boolean running = true;
            while (running) {
                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }

                while (!(fromServer = in.readLine()).equals("ASCII: OK")) {
                    System.out.println("Server: " + fromServer);
                    switch (fromServer) {
                        case "BYE: OK":
                        case "END: OK":
                            running = false;
                            break;
                    }

                    if (!running)
                        break;
                }

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

    private static boolean handShake(PrintWriter out, BufferedReader in)
        throws IOException {
        String handShakeMessage = "ASCII";
        System.out.println("Client: " + handShakeMessage);
        out.println(handShakeMessage);

        String response = null;
        while ((response = in.readLine()) == null) { }

        System.out.println("Server: " + response);

        return response.equals("ASCII: OK");
    }
}
