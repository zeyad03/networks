import java.io.*;
import java.net.*;

public class Client 
{
	public void connect()
	{
		try
		{
			Socket s = new Socket("localhost", 4242);

			// Buffer the input strem for performance.
			BufferedReader reader = new BufferedReader(
															new InputStreamReader(
																s.getInputStream()
															));
			String msg = reader.readLine();
			System.out.println("Action: " + msg);

			// Close reader and connection.
			reader.close();
			s.close();
		}
		catch( IOException e )
		{
			System.out.println(e);
		}
	}

	public static void main( String[] args )
	{
		Client client = new Client();
		client.connect();
	}
}