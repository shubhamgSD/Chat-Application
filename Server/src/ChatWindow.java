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
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ChatWindow extends javax.swing.JFrame {

    private String userName = "root";
    private String passWord = "sqlcm10";
    private Connection con;
    private Statement st;
    
    private InetAddress clientIP;
    private PrintWriter writer;
    private BufferedReader reader;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    
    private Thread incomingReaderThread;

    /**
     * Creates new form ChatWindow
     */
    public ChatWindow(InetAddress clientIP, PrintWriter writer, BufferedReader reader, ServerSocket serverSocket, Socket clientSocket) throws ClassNotFoundException, SQLException {
        
        this.clientIP = clientIP;
        this.writer = writer;
        this.reader = reader;
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        
        initComponents();

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        messageField.requestFocusInWindow();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
//        this.addWindowListener(new WindowAdapter(){
//            
//            public void windowClosing(WindowEvent windowEvent){
//                try {
//                    clientSocket.close();
//                    serverSocket.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//                try {
//                    con.close();
//                } catch (SQLException ex) {
//                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                System.exit(0);
//            }
//            
//        });

        clientInfoArea.append(this.clientIP.getHostName() + "\n" + this.clientIP.toString());

        String driver = "com.mysql.jdbc.Driver";

        Class.forName(driver);
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", userName, passWord);
        //System.out.println(con.toString());

        st = con.createStatement();
        st.executeUpdate("CREATE DATABASE IF NOT EXISTS chatDatabase;");

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatDatabase", userName, passWord);
        st = con.createStatement();
        st.executeUpdate("CREATE TABLE IF NOT EXISTS "
                + "entryTable(sender VARCHAR(10),"
                + "message VARCHAR(50));");

        //String query = "SELECT * FROM entryTable;";
        ResultSet rs = st.executeQuery("SELECT * FROM entryTable;");

        while (rs.next()) {
            String entry = rs.getString("sender") + " : " + rs.getString("message");
            this.writer.println(entry);
            this.writer.flush();
            messagesArea.append(entry + "\n");
        }
        
        messageField.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
                String message = messageField.getText();
                writer.println("Server : " + message);
                writer.flush();
                messagesArea.append("Server : " + message + "\n");
                
                try {
                    st.executeUpdate("INSERT INTO entryTable VALUES(\"Server\", \"" + message + "\");");
                } catch (SQLException ex) {
                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
                messageField.setText(null);
                
            }
        });
        
        incomingReaderThread = new Thread(new IncomingReader());
        incomingReaderThread.start();
        
    }

    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            try {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                
                String message;
                
                while ((message = reader.readLine()) != null) {
                    if(message.contains("CLIENT_DISCONNECTED")){
                        try {
                            clientSocket.close();
                            serverSocket.close();
                            con.close();
                            dispose();
                            CreateServer createServer = new CreateServer();

                        } catch (IOException ex1) {
                            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex1);
                        } catch (ClassNotFoundException ex1) {
                            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex1);
                        } catch (SQLException ex1) {
                            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }
                    else{
                        messagesArea.append("Client : " + message + "\n");

                        try {
                            st.executeUpdate("INSERT INTO entryTable VALUES(\"Client\", \"" + message + "\");");
                        } catch (SQLException ex) {
                            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
//                try {
//                    clientSocket.close();
//                    serverSocket.close();
//                    con.close();
//                    dispose();
//                    CreateServer createServer = new CreateServer();
//                    
//                } catch (IOException ex1) {
//                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex1);
//                } catch (ClassNotFoundException ex1) {
//                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex1);
//                } catch (SQLException ex1) {
//                    Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex1);
//                }
            }
            
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        messagesLabel = new javax.swing.JLabel();
        clientInfoLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        clientInfoArea = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        messagesArea = new javax.swing.JTextArea();
        messageField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        enterMessageLabel = new javax.swing.JLabel();
        disconnectClientButton = new javax.swing.JButton();
        deleteAllMessagesButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Chat-Application (Server side)");

        messagesLabel.setText("Messages :");

        clientInfoLabel.setText("Client Info :");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setHorizontalScrollBar(null);

        clientInfoArea.setEditable(false);
        clientInfoArea.setColumns(20);
        clientInfoArea.setRows(5);
        jScrollPane2.setViewportView(clientInfoArea);

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setHorizontalScrollBar(null);

        messagesArea.setEditable(false);
        messagesArea.setColumns(20);
        messagesArea.setRows(5);
        jScrollPane3.setViewportView(messagesArea);

        messageField.setBackground(new java.awt.Color(255, 255, 255));

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        enterMessageLabel.setText("Enter Message :");

        disconnectClientButton.setText("Disconnect Client");
        disconnectClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectClientButtonActionPerformed(evt);
            }
        });

        deleteAllMessagesButton.setText("Delete All Messages");
        deleteAllMessagesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteAllMessagesButtonActionPerformed(evt);
            }
        });

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(111, 111, 111)
                                .addComponent(disconnectClientButton))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(clientInfoLabel)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(messagesLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(enterMessageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(deleteAllMessagesButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(exitButton))
                            .addComponent(messageField, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(69, 69, 69)
                        .addComponent(sendButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(messagesLabel)
                    .addComponent(clientInfoLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(disconnectClientButton))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(messageField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendButton)
                    .addComponent(enterMessageLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteAllMessagesButton)
                    .addComponent(exitButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void deleteAllMessagesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteAllMessagesButtonActionPerformed
        try {
            // TODO add your handling code here:
            st.executeUpdate("DELETE FROM entryTable;");
        } catch (SQLException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        messagesArea.setText(null);
        
        writer.println("CLEAR_MESSAGES");
        writer.flush();
    }//GEN-LAST:event_deleteAllMessagesButtonActionPerformed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        // TODO add your handling code here:
        
        String message = messageField.getText();
        writer.println("Server : " + message);
        writer.flush();
        messagesArea.append("Server : " + message + "\n");

        try {
            st.executeUpdate("INSERT INTO entryTable VALUES(\"Server\", \"" + message + "\");");
        } catch (SQLException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        messageField.setText(null);
        
    }//GEN-LAST:event_sendButtonActionPerformed

    private void disconnectClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectClientButtonActionPerformed
        try {
            // TODO add your handling code here:
            
            writer.println("YOU_HAVE_BEEN_DISCONNECTED");
            writer.flush();
            
            clientSocket.close();
            serverSocket.close();
            con.close();
            dispose();
            CreateServer createServer = new CreateServer();
            
        } catch (IOException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_disconnectClientButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        try {
            // TODO add your handling code here:
            writer.println("SERVER_CLOSED");
            writer.flush();
            clientSocket.close();
            serverSocket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ChatWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
        
    }//GEN-LAST:event_exitButtonActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ChatWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ChatWindow().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea clientInfoArea;
    private javax.swing.JLabel clientInfoLabel;
    private javax.swing.JButton deleteAllMessagesButton;
    private javax.swing.JButton disconnectClientButton;
    private javax.swing.JLabel enterMessageLabel;
    private javax.swing.JButton exitButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField messageField;
    private javax.swing.JTextArea messagesArea;
    private javax.swing.JLabel messagesLabel;
    private javax.swing.JButton sendButton;
    // End of variables declaration//GEN-END:variables
}
