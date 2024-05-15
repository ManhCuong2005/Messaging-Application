/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Test;

import chat.app.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ACER
 */
public class ClientHandler implements Runnable{
    Socket socket;
    public static ArrayList<Socket> clients = new ArrayList<>();

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Mở luồng nhập cho Client
            DataInputStream input = new DataInputStream(socket.getInputStream());
            while (true) {
                // Đọc thông điệp từ Client
                String message = input.readUTF();
                saveMessageToDatabase(message);
                // Gửi thông điệp này đến tất cả các Client khác đã kết nối
                for (Socket client : clients) {
                    // Mở luồng xuất cho Client đích
                    DataOutputStream output = new DataOutputStream(client.getOutputStream());
                    // Gửi thông điệp
                    output.writeUTF(message);
                    output.flush();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Phương thức để lưu tin nhắn vào cơ sở dữ liệu
    private void saveMessageToDatabase(String message) {
        String connectionURL = "jdbc:sqlserver://localhost:1433;databaseName=Chat_Application;user=sa;password=cuong22052005;encrypt=true;trustServerCertificate=true;";
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Kết nối đến cơ sở dữ liệu
            connection = DriverManager.getConnection(connectionURL);

            // Chuẩn bị câu lệnh SQL để chèn tin nhắn vào bảng Message
            String sql = "INSERT INTO MessageTest (message) VALUES (?)";
            preparedStatement = connection.prepareStatement(sql);

            // Thiết lập giá trị cho tham số message
            preparedStatement.setString(1, message);

            // Thực thi câu lệnh SQL
            preparedStatement.executeUpdate();

            System.out.println("Message saved to database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối và tài nguyên
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
