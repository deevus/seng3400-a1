import java.io.*;
import java.net.*;

class CodeConverter {
    public static void main(String[] args) {
        int portNumber = 0;
        try {
            portNumber = args.length == 0 ? 1050 : Integer.parseInt(args[0]);
            if (portNumber < 1024) {
                System.err.println("Port number must be greater than 1023");
                System.exit(1);
            }
        } catch (Exception e ) {
            System.err.println("Usage: java CodeConverter <port number>");
            System.err.println("  <port number>: defaults to 1050");
            System.exit(1);
        }

        System.out.println("Hosting CodeConverter on port " + portNumber + "...");

        boolean ending = false;
        while (!ending) {
            ending = startServer(portNumber);
        }
    }

    static boolean startServer(int portNumber) {
        try (
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
            new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
        ) {

            String inputLine, outputLine;

            // Initiate conversation with client
            CodeProtocol protocol = new CodeProtocol();
            CodeProtocolState serverState = protocol.getState();
            while ((inputLine = in.readLine()) != null) {
                outputLine = protocol.processInput(inputLine);
                serverState = protocol.getState();
                out.println(outputLine);
            }

            return serverState == CodeProtocolState.End;
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

        //there was an error, so we'll end
        return true;
    }
}
