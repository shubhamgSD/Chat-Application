
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	Socket socket;
	BufferedReader reader;
	PrintWriter writer;
	InputStreamReader streamReader;
	
	public static void main(String[] args)throws Exception {
		Client client = new Client();
		client.go();
	}

	void go()throws Exception {
		try {

			socket = new Socket("127.0.1.1", 4996);
			streamReader = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(socket.getOutputStream());
			System.out.println("Client Networking Established:");
			
			Thread thread = new Thread(new IncomingReader());
			thread.start();

			BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
			String line;
			while (true) {
				System.out.print("Client: ");

				line = clientInput.readLine();// keyboard text Reading

				writer.println(line);// sending text to server
				writer.flush();
			}

			

		}

		catch (IOException ex) {
			ex.printStackTrace();
		}
		socket.close();
		streamReader.close();
		reader.close();
		writer.close();
		

	}

	public class IncomingReader implements Runnable {

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("Server: " + message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

}
