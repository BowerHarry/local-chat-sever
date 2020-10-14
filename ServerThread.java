/**
 * This class handles allowing the user to shut down the server.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerThread extends Thread {
    @Override
    public void run() {
        // Create new input reader.
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // Loop indefinitely.
        while(true) {
            try {
                // If the user enters "EXIT" the server is shut down.
                if ((reader.readLine()).equals("EXIT")){
                    System.out.println("Server shut down by user");
                    System.exit(0);
                }
            // Catch any exceptions and print out the error.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
