import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SwingContainerDemo  {
   private JFrame mainFrame;
   private JButton send;
   private JButton send2;
   private JButton exit;
   private JPanel topPanel;
   private JPanel bottomPanelC1;
   private JPanel bottomPanelC2;
   private JPanel centerPanel;
   private JLabel label1;
   private JLabel label2;
   private JTextArea display;
   private JTextArea info;
   private JTextField message;
   private JTextField file;
   private JScrollPane scrollerInfo;
   private JScrollPane scrollerDisplay;
   
   Socket socket;
	BufferedReader reader;
	PrintWriter writer;
	InputStreamReader streamReader;
   
   

   public SwingContainerDemo(){
	   
	      mainFrame = new JFrame("Title");
	      mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(),BoxLayout.Y_AXIS));
	      mainFrame.setSize(480,350); 
	      mainFrame.setVisible(true); 
	      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      
	      
	      topPanel = new JPanel();
	      centerPanel = new JPanel();
	      bottomPanelC1 = new JPanel();
	      bottomPanelC2 = new JPanel();
	      
	      message = new JTextField(20);
	      file = new JTextField("To send File(Temporary)",20);
	      label1 = new JLabel("       ");
	      label2 = new JLabel("       ");
	      send  = new JButton("Send");
	      send2 =  new JButton("Send");
	      exit = new JButton("Exit");
	      display = new JTextArea("To Display User Info",15,15);
	      display.setEditable(false);
	      info = new JTextArea("To Display Chat\n ",15,25);
	      info.setEditable(false);
	     scrollerInfo = new JScrollPane(info);
	     scrollerDisplay = new JScrollPane(display);
	     
	      bottomPanelC1.add(message);
	      bottomPanelC1.add(label1);
	      bottomPanelC1.add(send); 
	      bottomPanelC1.add(exit);
	      bottomPanelC2.add(file);
	      bottomPanelC2.add(label2);
	      bottomPanelC2.add(send2);
	      centerPanel.add(scrollerInfo);
	      centerPanel.add(scrollerDisplay);
	      
	      send.addActionListener(new SendButtonListener());
	     
	      mainFrame.getContentPane().add(topPanel);
	      mainFrame.getContentPane().add(centerPanel);
	      mainFrame.getContentPane().add(bottomPanelC1);
	      mainFrame.getContentPane().add(bottomPanelC2);
	      
   }
   
   void go()	throws Exception {
		try {

			socket = new Socket("127.0.1.1", 4005);
			streamReader = new InputStreamReader(socket.getInputStream());
			reader = new BufferedReader(streamReader);
			writer = new PrintWriter(socket.getOutputStream());
			System.out.println("Client Networking Established:");

		      Thread thread = new Thread(new IncomingReader());
				thread.start();
			}
		catch (IOException ex) {
				ex.printStackTrace();
		}

	}
   
   class IncomingReader implements Runnable {
		public  void run() {
			String message ;
			try {
				while ((message = reader.readLine()) != null) {
					info.append(message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}
   
public class SendButtonListener implements ActionListener{
public void actionPerformed(ActionEvent e) {
	try 
	{
		String str = message.getText();
		info.append(str+"\n");
		writer.println(str);
		writer.flush();
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	
}
}



}