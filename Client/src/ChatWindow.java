import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatWindow  {
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
   
   
    private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private InputStreamReader streamReader;
	
	private InetAddress serverIP;
   
   

   public ChatWindow(InetAddress serverIP){
	   
	   this.serverIP = serverIP;
	   
	      mainFrame = new JFrame("Title");
	      mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(),BoxLayout.Y_AXIS));
	      
	      Toolkit kit = Toolkit.getDefaultToolkit();
	      Dimension screenSize = kit.getScreenSize();
	      mainFrame.setSize(screenSize.width/2,screenSize.height/2); 
	      //mainFrame.setResizable(false);
	      
	      mainFrame.setTitle("Chat-Application (Client side)");
	       
	      //mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//              mainFrame.addWindowListener(new WindowAdapter(){
//                  @Override
//                  public void windowClosing(WindowEvent we){
//                      writer.println("CLIENT_DISCONNECTED");
//                      try {
//                          socket.close();
//                      } catch (IOException ex) {
//                          Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
//                      }
//                        System.exit(0);
//                  }
//              });
	      
	      
	      topPanel = new JPanel();
	      centerPanel = new JPanel();
	      bottomPanelC1 = new JPanel();
	      bottomPanelC2 = new JPanel();
	      
	      message = new JTextField(mainFrame.getWidth()/20);
	      file = new JTextField("To send File(Temporary)",20);
	      label1 = new JLabel("Message Box");
	      label2 = new JLabel("       ");
	      label3 = new JLabel("Chat box");
	      send  = new JButton("Send");
	      exit = new JButton("Exit");
	      login = new JButton("Login");
	      display = new JTextArea("User Info\n",mainFrame.getHeight()/20,mainFrame.getWidth()/30);
	      display.setEditable(false);
              display.append(serverIP.getHostName() + "\n" + serverIP.toString());
	      info = new JTextArea(mainFrame.getHeight()/20,mainFrame.getWidth()*2/30);
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
	      //bottomPanelC2.add(login);
	      //bottomPanelC2.add(label2);
	      bottomPanelC2.add(exit);
	     
	      
	      send.addActionListener(new SendButtonListener());
	      exit.addActionListener(new SendButtonListener());
	      
	      message.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				String str = message.getText();
				String entry = "Client: "+str+"\n";
				info.append(entry);
				writer.println(str);
				writer.flush();
				message.setText("");
				
			}
	      });
	     
	      mainFrame.getContentPane().add(topPanel);
	      mainFrame.getContentPane().add(centerPanel);
	      mainFrame.getContentPane().add(bottomPanelC1);
	      mainFrame.getContentPane().add(bottomPanelC2);
	      
	      mainFrame.pack();
              message.requestFocusInWindow();
              mainFrame.setLocationRelativeTo(null);
	      mainFrame.setVisible(true);
	      
   }
   
   void go()	throws Exception {
		try {

			socket = new Socket(serverIP, 4050);
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
                    if(message.contains("CLEAR_MESSAGES")){
                        info.setText(null);
                    }
                    else if(message.contains("YOU_HAVE_BEEN_DISCONNECTED")){
                        //reader.close();
                        socket.close();
                        info.append(message + "\n");
                        //System.exit(0);
                    }
                    else{
                        info.append(message + "\n");
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
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
                String entry = "Client : "+str+"\n";
                info.append(entry);
                writer.println(str);
                writer.flush();
                message.setText("");
            }
            else if(btn == exit)
            {
                writer.println("CLIENT_DISCONNECTED");
                writer.flush();
                socket.close();
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