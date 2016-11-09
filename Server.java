

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


class Server {
	BufferedReader reader;
	PrintWriter writer;
	InputStreamReader streamReader;
	ServerSocket serverSocket;
	Socket clientSocket;
	BufferedReader clientInput;
	
	public static void main(String[] args)throws Exception {
		Server server = new Server();
		server.go();

	}
	
	void go() throws Exception
	{
		serverSocket = new ServerSocket(4020);
		try
		{
			while(true)
			{
				clientSocket = serverSocket.accept();
				streamReader = new InputStreamReader(clientSocket.getInputStream());
				reader  = new BufferedReader(streamReader);
				writer = new PrintWriter(clientSocket.getOutputStream());
				System.out.println("Server Networking Established:");
				
				Thread thread = new Thread(new IncomingReader());
				thread.start();
				

				clientInput = new BufferedReader(new InputStreamReader(System.in));
				String line;
				while (true) {

					line = clientInput.readLine();// keyboard Reading

					
					if(line.equals("quit"))
					{
						serverSocket.close();
						clientSocket.close();
						reader.close();
						writer.close();
						streamReader.close();
					}
					writer.println(line);// sending text to server
					writer.flush();
				}
				
			

			}
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
		
	
	}
	
	public class IncomingReader implements Runnable {

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("client: " + message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}


}
