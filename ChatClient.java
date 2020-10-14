/**
 * This class is a client that when run can connect and communicate with a server.
 */

import java.io.*;
import java.net.Socket;

public class ChatClient {
    private final String serverName;
    private final int serverPort;
    // The socket identifier of the connection.
    protected Socket socket;
    // Input reader of messages coming from other clients.
    protected BufferedReader bufferedIn;
    // The output stream used to communicate with other clients.
    protected OutputStream serverOut;

    // Default constructor.
    public ChatClient(String _serverName, int _serverPort) {
        serverName  = _serverName;
        serverPort  = _serverPort;
    }

    // This method handles connecting and sending messages to a server.
    public static void main(String[] args) throws IOException {
        // Default server to connect to.
        String serverName = "localhost";
        // Default port to connect to.
        int serverPort = 14001;
        // Loop through every argument looking for "-cca" or "-ccp".
        for(int i=0; i<args.length; i++) {
            try {
                // If argument is "-cca" then assign the next argument to be the server name.
                if(args[i].equals("-cca")) {
                    serverName = args[++i];
                }
                // If argument is "-ccp" then assign the next argument to be the server name.
                if(args[i].equals("-ccp")){
                    serverPort = Integer.parseInt(args[++i]);
                }
            }
            // Catch any invalid arguments provided.
            catch (Exception e) {
                System.out.println("Invalid argument");
            }

        }
        // Create a new client with the server name and port.
        ChatClient client = new ChatClient(serverName,serverPort);
        // If connect() returns false then the connection has failed.
        if (!client.connect()){
            System.err.println("Connection failed");
        }
        // If the connection is successful...
        else {
            System.out.println("Connection successful");
            System.out.println("You can now message other users who have connected to the server");
            // Start reading the messages sent from other clients.
            client.startMessageLoop();
            // Create a reader that looks for user input.
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String input;
            // While there is input...
            while ((input = reader.readLine()) != null) {
                // Send the message to the server / other clients.
                client.sendMsg(input);
            }
        }
    }

    // This method handles sending messages to the server.
    protected void sendMsg(String input) throws IOException {
        // If the user enters "quit" the socket is closed and they leave the server.
        if ("quit".equalsIgnoreCase(input)) {
            socket.close();
            System.out.println("Successfully left the server");
            System.exit(0);
        }
        // Otherwise the message is written to the output stream.
        else {
            String message = input +"\n";
            serverOut.write(message.getBytes());
        }
}

    // This method starts a thread the constantly reads the input stream.
    protected void startMessageLoop() {
        // Creating a new thread that runs readMessageLoop().
        Thread t= new Thread(){
            @Override
            public void run() {
                readMessageLoop();
            }
        };
        t.start();
    }

    // This method reads the input stream from the server and outputs it to the user.
    protected void readMessageLoop() {
        try {
            String line;
            // Read the messages from bufferedIn (input stream).
            while ((line = bufferedIn.readLine()) != null) {
                // Print these messages to the user.
                System.out.println(line);
            }
        }
        // If the input stream ceases to exist then try to close the socket.
        catch (Exception ex) {
            ex.printStackTrace();
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // This method attempts to connect to client to the server.
    protected boolean connect() {
        try {
            // Try to create a new socket with the server name and port (connect to server).
            socket = new Socket(serverName, serverPort);
            // Fetch the output stream from the server.
            serverOut = socket.getOutputStream();
            // Fetch the input stream from the server.
            InputStream serverIn = socket.getInputStream();
            // Set up the reader for the input stream.
            bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
