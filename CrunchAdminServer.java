import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.FileReader;
import java.net.Socket;
import java.net.ServerSocket;

public class CrunchAdminServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server is Running on Port " + PORT);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("\nClient Connected: " + clientSocket.getInetAddress());

                    // Create a New Thread to Handle the Client Request
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    new Thread(clientHandler).start();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private static final String FILE_PATH = "Dictionary.txt";
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Setup Input and Output Streams for Communication
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read Client Request (Keywords and Options)
            String keyword = reader.readLine();
            //String options = reader.readLine();
            
            System.out.println("Client Request: " + keyword);

            // Generate Dictionary based on Keywords and Options
            String dict[] = getDefinitions(keyword);
            String result;
            if (dict != null) {
            	result = dict[0] + ": " + dict[1];
            	System.out.print("Result Found.\nPositive ");
            }
            else {
            	result = keyword + ": " + "Keyword Not Found in Dictionary!";
            	System.out.print("Result Not Found!\nNegative ");
            }
            
            // Send the Generated Dictionary back to the Client
            writer.println(result);
            System.out.println("Acknowledgement Sent to Client");
            
            // Close the Communication Resources
            reader.close();
            writer.close();
            clientSocket.close();
            System.out.println("Client Connection Closed Successfully.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getDefinitions(String keyword) {
    	String dict[] = new String[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String keywords = parts[0].trim();
                String definition = parts[1].trim();
                if (keywords.toLowerCase().contains(keyword.toLowerCase())) {
                	dict[0] = keywords;
                	dict[1] = definition;
                	return dict;
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error Reading Definitions File: " + e.getMessage());
        }
        return null;
    }
}
