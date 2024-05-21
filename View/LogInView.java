/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import java.io.DataOutputStream;
import javax.swing.JOptionPane;
import org.json.JSONObject;
import Client.Client;
import Client.Client;
import static Client.Client.jtfRoomId;
import Controller.MD5;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
/**
 *
 * @author ACER
 */
public class LogInView extends javax.swing.JFrame implements Runnable{
    public static DataOutputStream output;
    public static DataInputStream input;
    public static Socket socket;
    public static final String serverIP = "192.168.1.6"; //jtfPort.getText()
    public static DefaultListModel model;
    private DefaultListModel<String> messageListModel;
    private JList<String> messageList;
    static String name;
    /**
     * Creates new form LogInView
     */
    public LogInView() {
        initComponents();
        setLocationRelativeTo(null);
        model = new DefaultListModel();
        messageListModel = new DefaultListModel<>();
        messageList = new JList<>(messageListModel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextFieldMail = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Email:");

        jLabel2.setText("Password:");

        jButton1.setText("Đăng Nhập");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setText("Tạo tài khoản");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("ĐĂNG NHẬP");
        jLabel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Nếu bạn chưa có tài khoản, vui lòng tạo tài khoản trước khi Đăng nhập");

        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(206, 206, 206))
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldMail, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPasswordField1)))
                .addContainerGap(78, Short.MAX_VALUE))
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(207, 207, 207))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jTextFieldMail))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jPasswordField1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            JSONObject json = new JSONObject();
            // Lấy dữ liệu từ JPasswordField
            char[] passWord = jPasswordField1.getPassword();
            // Chuyển đổi mảng ký tự thành chuỗi để hiển thị (chỉ để demo, không an toàn trong thực tế)
            String passwordString = new String(passWord);
            String password;
            try {
                password = MD5.encrypt(passwordString);
                json.put("type", "login");
                json.put("email", jTextFieldMail.getText());
                json.put("password", password);

                System.out.println(json.toString());
                output.writeUTF(json.toString());
                output.flush();
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(LogInView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(LogInView.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(LogInView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
        CreateAccountView accountView = new CreateAccountView(socket);
        accountView.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

//    private JSONObject logIn() {
//        
//        return json;
//    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(LogInView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LogInView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LogInView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LogInView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LogInView().setVisible(true);
                try {
                    socket = new Socket(serverIP, 8888);
                    input = new DataInputStream(socket.getInputStream());
                    output = new DataOutputStream(socket.getOutputStream());
                    
                    LogInView inView = new LogInView();
                    Thread t = new Thread(inView);
                    t.start();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextFieldMail;
    // End of variables declaration//GEN-END:variables

    private void closeLogin() {
        dispose();
        Client c = new Client(socket);
        c.setVisible(true);
        Client.jLabelName.setText(name);
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                String response = input.readUTF();
                JSONObject jsonResponse = new JSONObject(response);
                String type = jsonResponse.getString("type");
                System.out.println("Thông điệp từ server: " + type);
                if (type.equals("login")) {
                    if (jsonResponse.getString("status").equals("success")) {
                        JOptionPane.showMessageDialog(null, "Đăng nhập thành công");
                        name = jsonResponse.getString("name");
                        System.out.println("Tên ở client hiện là: " + name);
                        closeLogin();
                    } else {
                        JOptionPane.showMessageDialog(null, "Sai tài khoản hoặc mật khẩu");
                    }
                } else if (type.equals("history_message")){
                    String message = jsonResponse.getString("message");
                    model.addElement(message);
                    Client.jList1.setModel(model);
    //                Cuộn xuống cuối danh sách tin nhắn
                    Client.jList1.ensureIndexIsVisible(model.getSize() - 1);
                } else if (type.equals("send_message")) {
                    String message = jsonResponse.getString("message");
                    model.addElement(message);
                    Client.jList1.setModel(model);
                    Client.jList1.ensureIndexIsVisible(model.getSize() - 1);
                } else if (type.equals("create_room")) {
                    if (jsonResponse.getString("status").equals("success")) {
                        JOptionPane.showMessageDialog(null, "Đã tạo phòng thành công!");
                    } else if (jsonResponse.getString("status").equals("failed")) {
                        JOptionPane.showMessageDialog(null, "ID phòng đã tồn tại!");
                    }
                } else if (type.equals("create_account")) {
                    if(jsonResponse.getString("status").equals("success")) {
                        JOptionPane.showMessageDialog(null, "Account created successfully!");
                        CreateAccountView.jTFEmailUser.setText("");
                        CreateAccountView.jTFNameUser.setText("");
                        CreateAccountView.jTFPasswordUser.setText("");
                    } else  if (jsonResponse.getString("status").equals("failed")){
                        JOptionPane.showMessageDialog(null, "Account exited!");
                    }
                    
                } else if (type.equals("confirmPassword")) {
                    if (jsonResponse.getString("status").equals("success")) {
                        JSONObject jsonRoomID = new JSONObject();
                        jsonRoomID.put("type", "history_message");
                        jsonRoomID.put("roomid", Client.roomId);
                        output.writeUTF(jsonRoomID.toString());
                        output.flush();
                    } else {
                        JOptionPane.showMessageDialog(null, "Mật khẩu sai!");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
