/**
 * Each instance of this class handles each a client connection.
 */
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerConnection extends Thread {
    // The socket identifier of this connection.
    private final Socket clientSocket;
    // The server identifier.
    private final Server server;
    // The output stream is used to communicate with other clients.
    private OutputStream outputStream;

    // Default constructor.
    public ServerConnection(Server _server, Socket _clientSocket) {
        clientSocket = _clientSocket;
        server = _server;
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        // Runs the method handleClientSocket and passes to it the name of this thread.
        try {
            handleClientSocket(threadName);
        // Catch any exceptions and print out the error message.
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // This method handles sending messages to other connected clients.
    private void handleClientSocket(String threadName) throws IOException {
        // Fetches the input and output streams of the server.
        InputStream inputStream = clientSocket.getInputStream();
        outputStream = clientSocket.getOutputStream();
        // Fetches the list of all the connected clients.
        List<ServerConnection> connectionList = server.getConnectionList();
        // Read input from the client handled by this thread.
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String input;
        // While the input is not empty...
        while ((input = reader.readLine()) != null) {
            String message = input +"\n";
            // For each client connected to the server...
            for(ServerConnection connection: connectionList) {
                // As long as that client is not the client handled by this thread...
                if (!connection.getName().equals(threadName)){
                    // Send the message to the other connected clients.
                    connection.send(message);
                }
            }
        }
        // The while loop only ends once the client has disconnected.
        System.out.println(clientSocket + " disconnected");
        // Remove that connection from the list of connected clients.
        server.removeConnection(this);
        // Close the socket.
        clientSocket.close();
    }

    // This method sends a message to another client.
    private void send(String message) throws IOException {
        outputStream.write(message.getBytes());
    }
}
