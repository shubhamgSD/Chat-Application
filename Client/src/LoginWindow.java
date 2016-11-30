
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.Action;

public class LoginWindow extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private final Action action = new SwingAction();
    private JLabel label_1;
    private JLabel label_2;
    private JLabel label_3;

    /**
     * Launch the application.
     */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					LoginWindow frame = new LoginWindow();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
    /**
     * Create the frame.
     */
    public LoginWindow() {

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

        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 640, 420);
        this.setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        textField = new JTextField();
        textField.setColumns(10);
        textField.requestFocusInWindow();

        textField_1 = new JTextField();
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setColumns(10);

        textField_3 = new JTextField();
        textField_3.setColumns(10);
        textField_3.addActionListener(action);

        JButton btnEnter = new JButton("Enter");
        btnEnter.setAction(action);

        JLabel lblEnterServersIp = new JLabel("Enter Server's IP Address :");

        JLabel label = new JLabel(".");

        label_1 = new JLabel(".");

        label_2 = new JLabel(".");

        label_3 = new JLabel(".");
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                        .addGap(48)
                        .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addGap(98)
                                        .addComponent(lblEnterServersIp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                                .addGroup(gl_contentPane.createSequentialGroup()
                                                        .addPreferredGap(ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                                                        .addComponent(label, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE))
                                                .addComponent(textField))
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(label_1, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE)
                                        .addGap(10)
                                        .addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)))
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(label_2, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE)
                        .addGap(7)
                        .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addComponent(btnEnter, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                                        .addGap(141))
                                .addGroup(gl_contentPane.createSequentialGroup()
                                        .addGap(5)
                                        .addComponent(textField_2, GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                        .addGap(7)
                                        .addComponent(label_3, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE)
                                        .addGap(7)
                                        .addComponent(textField_3)
                                        .addPreferredGap(ComponentPlacement.RELATED)))
                        .addGap(50))
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                        .addGap(96)
                        .addComponent(lblEnterServersIp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(67)
                        .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                                .addComponent(textField_1)
                                .addComponent(textField_2)
                                .addComponent(textField)
                                .addComponent(label_1)
                                .addComponent(label_2)
                                .addComponent(textField_3)
                                .addComponent(label_3))
                        .addGap(62)
                        .addComponent(btnEnter, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(label)
                        .addGap(64))
        );
        contentPane.setLayout(gl_contentPane);
    }

    private class SwingAction extends AbstractAction {

        public SwingAction() {
            putValue(NAME, "Enter");
            //putValue(SHORT_DESCRIPTION, "Some short description");
        }

        public void actionPerformed(ActionEvent e) {
            InetAddress serverIP = null;
            try {
                serverIP = InetAddress.getByName(textField.getText() + "." + textField_1.getText() + "." + textField_2.getText() + "." + textField_3.getText());
            } catch (UnknownHostException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            ChatWindow chatWindow = null;
            chatWindow = new ChatWindow(serverIP);
            try {
                chatWindow.go();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            dispose();

        }
    }
}
