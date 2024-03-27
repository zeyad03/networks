import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 9100;
    private static final String SERVER_FILES_DIRECTORY = "serverFiles";
    private static final String LOG_FILE = "log.txt";

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String command = in.readLine();

                if (command.equals("list")) {
                    listFiles(out);
                    logRequest(socket, command);
                } else if (command.equals("put")) {
                    String fileName = in.readLine();
                    uploadFile(in, fileName, out);
                    logRequest(socket, command);
                } else {
                    out.println("Invalid command");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void listFiles(PrintWriter out) {
            File directory = new File(SERVER_FILES_DIRECTORY);
            File[] files = directory.listFiles();
            if (files != null) {
                out.println("Listing " + files.length + " file(s):");
                for (File file : files) {
                    out.println(file.getName());
                }
            }
        }

        private void uploadFile(BufferedReader in, String fileName, PrintWriter out) {
					try {
							Path filePath = Paths.get(fileName);
							if (!Files.exists(filePath)) {
									out.println("Error: File '" + fileName + "' not found.");
									return;
							}

							Path destPath = Paths.get(SERVER_FILES_DIRECTORY, filePath.getFileName().toString());
							if (Files.exists(destPath)) {
									out.println("Error: Cannot upload file '" + filePath.getFileName() + "'; file with the same name already exists on server.");
									return;
							}

							Files.copy(filePath, destPath);
							out.println("Uploaded file " + filePath.getFileName());
					} catch (IOException e) {
							e.printStackTrace();
							out.println("Error: Failed to upload file");
					}
				}

        private void logRequest(Socket socket, String request) {
					try (PrintWriter logWriter = new PrintWriter(new FileWriter(LOG_FILE, true))) {
							String dateTime = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss").format(new Date());
							String clientAddress = socket.getInetAddress().getHostAddress();
							logWriter.println(dateTime + "|" + clientAddress + "|" + request);
					} catch (IOException e) {
							e.printStackTrace();
					}
				}			
    }
}