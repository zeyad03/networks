import java.io.*;
import java.net.*;

public class Client {

    private static final int SERVER_PORT = 9100;
    private static final String SERVER_HOST = "localhost";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java Client <command>");
            System.exit(1);
        }

        String command = args[0];

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(command);

            if (command.equals("list")) {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
            } else if (command.equals("put")) {
                if (args.length != 2) {
                    System.err.println("Usage: java Client put <filename>");
                    System.exit(1);
                }

                String fileName = args[1];
                put(fileName, out);
                System.out.println(in.readLine()); // Print server response
            } else {
                System.err.println("Invalid command.");
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void put(String fileName, PrintWriter out) {
			// Send the file name to the server
			out.println("../client/" + fileName);
	
			try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
					String line;
					while ((line = fileReader.readLine()) != null) {
							out.println(line);
					}
					// Send an empty line to signal the end of file
					out.println();
			} catch (FileNotFoundException e) {
					System.err.println("Error: File not found.");
					System.exit(1);
			} catch (IOException e) {
					System.err.println("Error reading file: " + e.getMessage());
					System.exit(1);
			}
	}
}
