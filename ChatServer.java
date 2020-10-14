/**
 * When this class is run, the chat server will go online.
 */
public class ChatServer {
    public static void main(String[] args) {
        // The default port is 14001.
        int port = 14001;
        // Loops through each argument provided to the program.
        for(int i=0; i<args.length; i++) {
            // If an argument is "-csp" then the next argument is the port number.
            if(args[i].equals("-csp")){
                try {
                    // Checks if it is a valid port number.
                    if ((Integer.parseInt(args[i + 1]) <= 65535) && (Integer.parseInt(args[i + 1]) >=0)){
                        // Assigns the new port number.
                        port = Integer.parseInt(args[++i]);
                    }
                    else {
                        System.out.println("Argument provided was not valid");
                    }
                }
                // Catches if the argument after "-csp" is not a integer.
                catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Argument provided was not valid");
                }
            }
        }
        // Creates a new server and assigns it a port.
        Server server = new Server(port);
        System.out.println("Sever started on port " + port);
        // Run the server thread.
        server.start();
    }
}
