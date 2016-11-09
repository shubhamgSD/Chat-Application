import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class SwingContainerDemo  {
   private JFrame mainFrame;
   private JButton send;
   private JButton send2;
   private JButton exit;
   private JButton login;
   private JPanel topPanel;
   private JPanel bottomPanelC1;
   private JPanel bottomPanelC2;
   private JPanel centerPanel;
   private JLabel label1;
   private JLabel label2;
   private JLabel label3;
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
	      mainFrame.setMaximumSize(new Dimension(500,500));
	      
	      
	      topPanel = new JPanel();
	      centerPanel = new JPanel();
	      bottomPanelC1 = new JPanel();
	      bottomPanelC2 = new JPanel();
	      
	      message = new JTextField(20);
	      file = new JTextField("To send File(Temporary)",20);
	      label1 = new JLabel("Message Box");
	      label2 = new JLabel("       ");
	      label3 = new JLabel("Chat box");
	      send  = new JButton("Send");
	      exit = new JButton("Exit");
	      login = new JButton("Login");
	      display = new JTextArea("User Info\n",15,15);
	      display.setEditable(false);
	      info = new JTextArea(15,25);
	      info.setEditable(false);
	     scrollerInfo = new JScrollPane(info);
	     scrollerDisplay = new JScrollPane(display);
	     
	     Border border = BorderFactory.createLineBorder(Color.BLACK);
	     info.setBorder(border);
	     display.setBorder(border);
	     message.setBorder(border);
	     
	     topPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
	     send.setBackground(Color.LIGHT_GRAY);
	     exit.setBackground(Color.LIGHT_GRAY);
	     login.setBackground(Color.LIGHT_GRAY);
	     
	     
	     
	     topPanel.add(label3);
	     centerPanel.add(scrollerInfo);
	      centerPanel.add(scrollerDisplay);
	     bottomPanelC1.add(label1);
	      bottomPanelC1.add(message);
	      bottomPanelC1.add(send); 
	      bottomPanelC2.add(login);
	      bottomPanelC2.add(label2);
	      bottomPanelC2.add(exit);
	     
	      
	      send.addActionListener(new SendButtonListener());
	      exit.addActionListener(new SendButtonListener());
	     
	      mainFrame.getContentPane().add(topPanel);
	      mainFrame.getContentPane().add(centerPanel);
	      mainFrame.getContentPane().add(bottomPanelC1);
	      mainFrame.getContentPane().add(bottomPanelC2);
	      
   }
   
   void go()	throws Exception {
		try {

			socket = new Socket("127.0.1.1", 4020);
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
					info.append("Sender: "+message+"\n");
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
	JButton btn =(JButton) e.getSource();
	if(btn==send){
		String str = message.getText();
		info.append("Me: "+str+"\n");
		writer.println(str);
		writer.flush();
		message.setText("");
	}
	else if(btn == exit)
	{
		System.exit(0);
	}
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	
}
}



}
