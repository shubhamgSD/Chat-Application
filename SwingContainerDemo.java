import java.awt.Dimension;

import javax.swing.*;

public class SwingContainerDemo {
    private JFrame mainFrame;
    private JButton send;
    private JButton send2;
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

        message = new JTextField("To send Message",20);
        file = new JTextField("To send File(Temporary)",20);
        label1 = new JLabel("       ");
        label2 = new JLabel("       ");
        send  = new JButton("Send");
        send2 =  new JButton("Send");
        display = new JTextArea("To Display User Info",15,15);
        info = new JTextArea("To Display Chat ",15,25);
        label3 = new JLabel("     ");
        scrollerInfo = new JScrollPane(info);
        scrollerDisplay = new JScrollPane(display);
	     

        bottomPanelC1.add(message);
        bottomPanelC1.add(label1);
        bottomPanelC1.add(send);
        bottomPanelC2.add(file);
        bottomPanelC2.add(label2);
        bottomPanelC2.add(send2);
        centerPanel.add(scrollerInfo);
        centerPanel.add(scrollerDisplay);

        mainFrame.getContentPane().add(topPanel);
        mainFrame.getContentPane().add(centerPanel);
        mainFrame.getContentPane().add(bottomPanelC1);
        mainFrame.getContentPane().add(bottomPanelC2);

    }
      
      
}