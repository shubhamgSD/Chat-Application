

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.sql.*;


class Server {
	private BufferedReader reader;
	private PrintWriter writer;
	private InputStreamReader streamReader;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private BufferedReader clientInput;
	
	private String userName = "root";
	private String passWord = "sqlcm10";
	
	private Connection con;
	
	public static void main(String[] args)throws Exception {
		Server server = new Server();
		server.go();

	}
	
	void go() throws Exception
	{
		serverSocket = new ServerSocket(4055);
		try
		{
			while(true)
			{
				clientSocket = serverSocket.accept();
				streamReader = new InputStreamReader(clientSocket.getInputStream());
				reader  = new BufferedReader(streamReader);
				writer = new PrintWriter(clientSocket.getOutputStream());
				System.out.println("Server Networking Established:");
				
				String driver = "com.mysql.jdbc.Driver";

			      Class.forName(driver);
			      con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", userName, passWord);
			      //System.out.println(con.toString());
			      
			      Statement st = con.createStatement();
			      st.executeUpdate("CREATE DATABASE IF NOT EXISTS chatDatabase;");
			      
			      con = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatDatabase", userName, passWord);
			      st = con.createStatement();
			      st.executeUpdate("CREATE TABLE IF NOT EXISTS " +
			      "entryTable(sender VARCHAR(10)," +
			    		  "message VARCHAR(50));");
			      
			      String query = "SELECT * FROM entryTable;";
			      ResultSet rs = st.executeQuery(query);
			      
			      while(rs.next()){
			    	  String entry = rs.getString("sender") + " : " + rs.getString("message");
			    	  writer.println(entry);
			    	  writer.flush();
			    	  System.out.println(entry);
			      }
				
				Thread thread = new Thread(new IncomingReader());
				thread.start();
				

				clientInput = new BufferedReader(new InputStreamReader(System.in));
				String line;
				while (true) {

					line = clientInput.readLine();// keyboard Reading
					if(line.equals(null))
						continue;
					
					if(line.equals("quit"))
					{
						serverSocket.close();
						clientSocket.close();
						reader.close();
						writer.close();
						streamReader.close();
						System.exit(0);
					}
					writer.println("Server : " + line);// sending text to server
					writer.flush();
					
					st.executeUpdate("INSERT INTO entryTable VALUES(\"Server\", \"" + line + "\");");
					
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
			Statement st = null;
			try {
				st = con.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				while ((message = reader.readLine()) != null) {
					System.out.println("client : " + message);
					
					st.executeUpdate("INSERT INTO entryTable VALUES(\"Client\", \"" + message + "\");");
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}


}
