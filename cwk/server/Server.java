import java.io.*;
import java.net.*;
import java.util.*;

public class Server 
{

	public void runServer()
	{
		try
		{
			// For the example in lectures, we use port 4242.
			ServerSocket serverSock = new ServerSocket(4242);

			while( true )
			{
				// Accept; blocking; will not return until a client has made contact.
				Socket sock = serverSock.accept();

				// Get information about the connection.
				InetAddress inet = sock.getInetAddress();
				Date date = new Date();
				System.out.println("\nDate " + date.toString());
				System.out.println("Connection made from " + inet.getCanonicalHostName());

				// Imitate uploading files.
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				writer.println("File has been uploaded.\n");
				writer.close();
				sock.close();
			}
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}

	public static void main( String[] args )
	{
		Server server = new Server();
		server.runServer();
	}
}