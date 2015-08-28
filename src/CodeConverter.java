import java.io.*;
import java.net.*;

class CodeConverter {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java CodeConverter <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

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
