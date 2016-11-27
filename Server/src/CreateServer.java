/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chirag
 */

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateServer {
    
    BufferedReader reader;
    PrintWriter writer;
    InputStreamReader streamReader;
    ServerSocket serverSocket;
    Socket clientSocket;
    BufferedReader clientInput;
    InetAddress clientIP;
    
    
    CreateServer() throws IOException, ClassNotFoundException, SQLException{
        
        serverSocket = new ServerSocket(4050);
        JLabel waitLabel = new JLabel("Waiting for client on port " + serverSocket.getLocalPort() + "...");
        waitLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        JFrame waitFrame = new JFrame();
        waitFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        waitFrame.setSize(400, 200);
        waitFrame.setLayout(new BoxLayout(waitFrame.getContentPane(), BoxLayout.Y_AXIS));
        
        waitFrame.setResizable(false);
        waitFrame.add(waitLabel);
        waitFrame.add(exitButton);
        waitFrame.setLocationRelativeTo(null);
        waitFrame.setVisible(true);
        exitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

                    serverSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(CreateServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(0);
            }
            
        });
        //JOptionPane.showMessageDialog(null, waitLabel);
        
        clientSocket = serverSocket.accept();
        streamReader = new InputStreamReader(clientSocket.getInputStream());
        reader  = new BufferedReader(streamReader);
        writer = new PrintWriter(clientSocket.getOutputStream());
        clientIP = clientSocket.getInetAddress();
        System.out.println("Server Networking Established:");
        
//        Window win = SwingUtilities.getWindowAncestor(waitLabel);
//        win.dispose();

        waitFrame.dispose();
        
        ChatWindow chatWindow = new ChatWindow(clientIP, writer, reader, serverSocket, clientSocket);
        
    }
    
}
