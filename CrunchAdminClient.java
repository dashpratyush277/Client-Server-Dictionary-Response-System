import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CrunchAdminClient {
    private static final String SERVER_IP = "localhost"; // Add IPv4 Address of Different Computer on Same Network to Run the Client Side Program on Different Computers.
    private static final int SERVER_PORT = 12345; // Add Port of the Server Computer on the Network

    public static void main(String[] args) {
    	Scanner sc=new Scanner(System.in);
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);

            // Setup Input and Output Streams for Communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            
            System.out.println("Client Server Based Dictionary Generator");
            System.out.println("----------------------------------------");
            System.out.print("Enter the Keyword to Search: ");
            String keyword = sc.nextLine();
            
            // Send Keywords and Options to the Server
            writer.println(keyword);
            //writer.println("Option1,Option2"); // Replace with Actual Options

            // Receive and Print the Generated Dictionary from the Server
            String dictionary = reader.readLine();
            System.out.println("Dictionary from Server:-\n" + dictionary);

            // Close the Communication Resources
            reader.close();
            writer.close();
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        sc.close();
    }
}
