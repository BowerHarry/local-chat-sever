/**
 * This class is in charge of accepting new client connections.
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
    private final int serverPort;
    // A array that will contain every client currently connected to the server.
    private ArrayList<ServerConnection> connectionList = new ArrayList<>();

    public Server(int serverPort){
        // The argument passed to this class is the server port.
        this.serverPort = serverPort;
    }

    // This method returns the list of currently connected clients.
    public List<ServerConnection> getConnectionList(){
        return connectionList;
    }


    @Override
    // Server thread.
    public void run() {
        // Creates a new thread that checks user input, "EXIT" will shut down the server.
        ServerThread t = new ServerThread();
        t.start();
        try {
            // Create a new server socket.
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while(true) {
                // Wait for a client to attempt to connect.
                System.out.println("Waiting for a new connection...");
                // Accept any client that attempts to connect.
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected to " + clientSocket);
                // Creates a new thread that handles the newly connected client.
                ServerConnection connection = new ServerConnection(this, clientSocket);
                connection.start();
                // Add the new client to the list of all clients.
                connectionList.add(connection);
            }
        // Catches any exceptions and prints out the error message.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This procedure removes the client passed to it from the list of clients.
    public void removeConnection(ServerConnection connection) {
        connectionList.remove(connection);
    }


}
