package Client;

import Controller.MD5;
import Server.app.JDBC.JDBCUtil;
import View.CreateAccountView;
import View.LogInView;
import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import java.sql.PreparedStatement;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.json.JSONObject;
//import org.json.JSONObject;

public class Client extends javax.swing.JFrame /*implements Runnable*/ {
    public static Socket socket;
    public static DataOutputStream output;
    public static DataInputStream input;
    DefaultListModel model;
    String name;
    public static String roomId;
    public static String email;

    public Client(Socket socket) {
        initComponents();
        setLocationRelativeTo(null);
        model = LogInView.model;
        this.socket = socket;
        try {
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jListMessage.setModel(model);
        
        jList.setModel(model);
        
//        jList.addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    
//                    try {
//                        // Lấy phần tử đã chọn
//                        String selectedValue = jList.getSelectedValue();
//                        System.out.println("Selected: " + selectedValue);
//
//                        model.clear();
//                        jListMessage.setModel(model);
//
//                        JSONObject jsonConfirmPasswordRoom = new JSONObject();
//                        jsonConfirmPasswordRoom.put("type", "history_message");
//                        jsonConfirmPasswordRoom.put("roomId", selectedValue);
//                        jsonConfirmPasswordRoom.put("email", email);
//                        output.writeUTF(jsonConfirmPasswordRoom.toString());
//                        output.flush();
//                    } catch (IOException ex) {
//                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//        });

        jList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    try {
                        String selectedValue = jList.getSelectedValue();

                        if (selectedValue != null) {
                            System.out.println("Selected: " + selectedValue);

                            model.clear();
                            jListMessage.setModel(model);

                            JSONObject jsonConfirmPasswordRoom = new JSONObject();
                            jsonConfirmPasswordRoom.put("type", "history_message");
                            jsonConfirmPasswordRoom.put("roomId", selectedValue);
                            jsonConfirmPasswordRoom.put("email", email);
                            output.writeUTF(jsonConfirmPasswordRoom.toString());
                            output.flush();
                            
                            jtfRoomId.setText(selectedValue);
                        } else {
                            System.out.println("Selected value is null");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

    }

    private JSONObject createMessageJSON() {
        JSONObject json = new JSONObject();
        if (Client.jtareaMess.getText() != null) {
            json.put("type", "send_message");
            json.put("roomId", jtfRoomId.getText());
            json.put("message", (name + ": " + jtareaMess.getText()));
        }
        return json;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jtfRoomId = new javax.swing.JTextField();
        jButtonJoin = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtareaMess = new javax.swing.JTextArea();
        jButtonSend = new javax.swing.JButton();
        jLabelName = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListMessage = new javax.swing.JList<>();
        jLabelEmail = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList = new javax.swing.JList<>();
        jSeparator3 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("ROOM ID:");

        jtfRoomId.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jtfRoomId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfRoomIdActionPerformed(evt);
            }
        });

        jButtonJoin.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jButtonJoin.setText("Tham gia");
        jButtonJoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJoinActionPerformed(evt);
            }
        });

        jtareaMess.setColumns(20);
        jtareaMess.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        jtareaMess.setRows(3);
        jScrollPane2.setViewportView(jtareaMess);

        jButtonSend.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonSend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/send-icon1.png"))); // NOI18N
        jButtonSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendActionPerformed(evt);
            }
        });

        jLabelName.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabelName.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jButton3.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jButton3.setText("Tạo Phòng");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jListMessage.setFont(new java.awt.Font("Times New Roman", 0, 20)); // NOI18N
        jScrollPane3.setViewportView(jListMessage);

        jLabelEmail.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabelEmail.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel2.setText("Tên:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jLabel3.setText("Email:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSend, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtfRoomId, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabelName, javax.swing.GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
                                    .addComponent(jLabelEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonJoin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelName, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonJoin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtfRoomId)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButtonSend, javax.swing.GroupLayout.PREFERRED_SIZE, 45, Short.MAX_VALUE))
                .addContainerGap())
        );

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("MESSAGE LIST");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jList.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        jScrollPane1.setViewportView(jList);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator3))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonJoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJoinActionPerformed
        roomId = jtfRoomId.getText();
        boolean isDuplicate = false;
        
        try {
            // Kiểm tra trùng lặp
            Enumeration<String> elements = LogInView.messageListModel.elements();
            while (elements.hasMoreElements()) {
                if (roomId.equals(elements.nextElement())) {
                    isDuplicate = true;
                    break;
                }
            }

            if (isDuplicate) {
                JOptionPane.showMessageDialog(null, "Bạn đã tham gia phòng này rồi!");
            } else {
                model.clear();
                jListMessage.setModel(model);
                String passWord = JOptionPane.showInputDialog(null, "confirm");

                if (passWord != null) {
                    String password = MD5.encrypt(passWord);
                    JSONObject jsonConfirmPasswordRoom = new JSONObject();
                    jsonConfirmPasswordRoom.put("type", "ConfirmPasswordRoom");
                    jsonConfirmPasswordRoom.put("roomId", roomId);
                    jsonConfirmPasswordRoom.put("password", password);
                    jsonConfirmPasswordRoom.put("email", email);
                    output.writeUTF(jsonConfirmPasswordRoom.toString());
                    output.flush();
                }
            }
        } catch (Exception e) {
        }

//        try (Connection connection = JDBCUtil.getConnection()) {
//            roomId = jtfRoomId.getText();
//            if (roomId.equals("")) {
//                JOptionPane.showMessageDialog(this, "Vui lòng nhập Room ID");
//            } else {
//                String sql = "SELECT * FROM User_Groups WHERE roomID = ? and email = ?";
//                PreparedStatement pst = connection.prepareStatement(sql);
//                pst.setString(1, roomId);
//                pst.setString(2, email);
//                ResultSet rs = pst.executeQuery();
//                if (rs.next()) {
//                    JOptionPane.showMessageDialog(this, "Bạn đã ở trong phòng này");
//                } else {
//                    String query = "INSERT INTO User_Groups(roomID, email) VALUES (?, ?)";
//                    PreparedStatement statement = connection.prepareStatement(query);
//                    statement.setString(1, roomId);
//                    statement.setString(2, email);
//                    statement.executeUpdate();
//                }
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_jButtonJoinActionPerformed

    private void jButtonSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendActionPerformed
        try {
            name = jLabelName.getText();
            // Tạo đối tượng JSONObject để biểu diễn tin nhắn
            JSONObject json = createMessageJSON();
            // TODO add your handling code here:
            output.writeUTF(json.toString());
            output.flush();
            jtareaMess.setText("");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonSendActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
//        dispose();
        View.CreateRoomView createRoom = new View.CreateRoomView(socket);
        createRoom.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jtfRoomIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfRoomIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfRoomIdActionPerformed

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
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client(socket).setVisible(true);
                
            }
        });
    }
//        Thread.startVirtualThread(task)
//    }
//    
//    @Override
//    public void run() {
//        try {
//            while (true) {
//                String response = input.readUTF();
//                JSONObject jsonResponse = new JSONObject(response);
//                String type = jsonResponse.getString("type");
//                System.out.println("Thông điệp từ server: " + type);
//                if (type.equals("login")) {
//                    if (jsonResponse.getString("status").equals("success")) {
//                        JOptionPane.showMessageDialog(null, "Đăng nhập thành công");
//                        name = jsonResponse.getString("name");
//                        System.out.println("Tên ở client hiện là: " + name);
//                        jLabelName.setText(name);
//                        dispose();
//                        Client c = new Client();
//                        c.setVisible(true);
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Sai tài khoản hoặc mật khẩu");
//                    }
//                } else if (type.equals("message_history")){
////                    String message = input.readUTF();
////                    String jsonString  = input.readUTF();
////                    JSONObject receivedJson = new JSONObject(response );
//                    String message = jsonResponse.getString("message");
//                    model.addElement(message);
//                    jListMessage.setModel(model);
//    //                Cuộn xuống cuối danh sách tin nhắn
//                    jListMessage.ensureIndexIsVisible(model.getSize() - 1);
//                } else if (type.equals("send_message")) {
//                    String message = jsonResponse.getString("message");
//                    model.addElement(message);
//                    jListMessage.setModel(model);
//                    jListMessage.ensureIndexIsVisible(model.getSize() - 1);
//                } else if (type.equals("create_room")) {
//                    if (jsonResponse.getString("status").equals("success")) {
//                        JOptionPane.showMessageDialog(null, "Đã tạo phòng thành công!");
//                    } else if (jsonResponse.getString("status").equals("failed")) {
//                        JOptionPane.showMessageDialog(null, "ID phòng đã tồn tại!");
//                    }
//                } else if (type.equals("create_account")) {
//                    if(jsonResponse.getString("status").equals("success")) {
//                        JOptionPane.showMessageDialog(null, "Account created successfully!");
//                        View.CreateAccountView.jTFEmailUser.setText("");
//                        View.CreateAccountView.jTFNameUser.setText("");
//                        View.CreateAccountView.jTFPasswordUser.setText("");
//                    } else  if (jsonResponse.getString("status").equals("failed")){
//                        JOptionPane.showMessageDialog(null, "Account exited!");
//                    }
//                    
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonJoin;
    private javax.swing.JButton jButtonSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public static javax.swing.JLabel jLabelEmail;
    public static javax.swing.JLabel jLabelName;
    public static javax.swing.JList<String> jList;
    public static javax.swing.JList<String> jListMessage;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    public static javax.swing.JTextArea jtareaMess;
    public static javax.swing.JTextField jtfRoomId;
    // End of variables declaration//GEN-END:variables
}
