package Server.app;

import static Server.app.Server.input;
import static Server.app.Server.output;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ClientHandler implements Runnable{
    Socket socket;
    String roomId;
    public static ArrayList<Socket> clients = new ArrayList<>();
    public static Map<String, List<ClientHandler>> rooms = new HashMap<>();
//    static DataOutputStream output;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try /*(DataInputStream input = new DataInputStream(socket.getInputStream()); DataOutputStream output = new DataOutputStream(socket.getOutputStream()))*/{
            
//            String roomId = input.readUTF();
            this.roomId = Server.roomID;
//            getMessagesByRoomID(roomId, Server.output);
            addClientToRoom(roomId, this);
            
            while (true) { 
                String jsonString  = input.readUTF();
                
                JSONObject receivedJson = new JSONObject(jsonString );
                String type = (String) receivedJson.get("type");
                
                if (type.equals("send_message")) {
                    String roomId2 = receivedJson.getString("roomId");
                    String message = receivedJson.getString("message");
                    sendMessageToRoom(roomId2, message);
                    saveMessageToDatabase(receivedJson);
                } else if (type.equals("create_room")){
                    saveDataRoomToDatabase(receivedJson);
                } else if (type.equals("create_account")) {
                    saveAccountToDatabase(receivedJson);
                } else if (type.equals("login")) {
                    if (checkAccount(receivedJson)) {
                        output.writeUTF(new JSONObject().put("type", "log_in").toString());
                        output.flush();
                        System.out.println("Đã gởi log_in");
                    } else {
                        output.writeUTF(new JSONObject().put("type", "failed").toString());
                        output.flush();
                        System.out.println("Đã gởi failed");
                    }
                } 
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            removeClientFromRoom(roomId, this); 
        }
    }
    
    public static synchronized void removeClientFromRoom(String roomId, ClientHandler clientHandler) {
        List<ClientHandler> clientsInRoom = rooms.get(roomId);
        if (clientsInRoom != null) {
            clientsInRoom.remove(clientHandler);
            if (clientsInRoom.isEmpty()) {
                rooms.remove(roomId);
            }
        } else {
            System.out.println("null");
        }
    }
    
    public static synchronized void sendMessageToRoom(String roomId, String message) {
        List<ClientHandler> clientsInRoom = rooms.get(roomId);
        if (clientsInRoom != null) {
            for (ClientHandler client : clientsInRoom) {
                try {
                    output = new DataOutputStream(client.socket.getOutputStream());
                    output.writeUTF(message);
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static synchronized void addClientToRoom(String roomId, ClientHandler clientHandler) {
        List<ClientHandler> clientsInRoom = rooms.get(roomId);
        if (clientsInRoom == null) {
            clientsInRoom = new ArrayList<>();
            rooms.put(roomId, clientsInRoom);
        }
        clientsInRoom.add(clientHandler);
    }
    
    // Phương thức để lưu tin nhắn từ JSON vào cơ sở dữ liệu
    public void saveMessageToDatabase(JSONObject json) {
        try (Connection connection = chat.app.JDBC.JDBCUtil.getConnection()) {
            // Chuẩn bị câu lệnh SQL để chèn tin nhắn vào bảng Message
            String sql = "INSERT INTO Message (RoomID, Message) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Thiết lập giá trị cho các tham số từ JSON
                preparedStatement.setString(1, json.getString("roomId"));
                preparedStatement.setString(2, json.getString("message"));

                // Thực thi câu lệnh SQL
                preparedStatement.executeUpdate();
                
                System.out.println("Message saved to database successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Phương thức để lưu tin nhắn từ JSON vào cơ sở dữ liệu
    public void saveAccountToDatabase(JSONObject json) {
        try (Connection connection = chat.app.JDBC.JDBCUtil.getConnection()) {
            // Chuẩn bị câu lệnh SQL để chèn tin nhắn vào bảng Message
            String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Thiết lập giá trị cho các tham số từ JSON
                preparedStatement.setString(1, json.getString("name"));
                preparedStatement.setString(2, json.getString("email"));
                preparedStatement.setString(3, json.getString("password"));

                // Thực thi câu lệnh SQL
                preparedStatement.executeUpdate();
                
                System.out.println("Account saved to database successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Phương thức để lưu tin nhắn từ JSON vào cơ sở dữ liệu
    public void saveDataRoomToDatabase(JSONObject json) {
        try (Connection connection = chat.app.JDBC.JDBCUtil.getConnection()) {
            // Chuẩn bị câu lệnh SQL để chèn tin nhắn vào bảng Message
            String sql = "INSERT INTO RoomID (RoomID, PassWord) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Thiết lập giá trị cho các tham số từ JSON
                preparedStatement.setString(1, json.getString("roomId"));
                preparedStatement.setString(2, json.getString("password"));

                // Thực thi câu lệnh SQL
                preparedStatement.executeUpdate();
                
                String result = "Room saved to database successfully!";
                System.out.println(result);
                
                try {
//                    output = new DataOutputStream(socket.getOutputStream());
                    output.writeUTF("success");
                    output.flush();
                    System.out.println("Gởi dữ liệu thành công");
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean checkAccount(JSONObject json) {
        boolean isAuthenticated = false;
        try (Connection connection = chat.app.JDBC.JDBCUtil.getConnection()) {
            // Chuẩn bị câu lệnh SQL để chèn tin nhắn vào bảng Message
            String sql = "SELECT 1 FROM users WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Thiết lập giá trị cho các tham số từ JSON
                preparedStatement.setString(1, json.getString("email"));
                preparedStatement.setString(2, json.getString("password"));

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    // Kiểm tra nếu có bản ghi nào được trả về
                    isAuthenticated = rs.next();
                    if (isAuthenticated) {
                        System.out.println("kiểm tra dữ liệu ok");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAuthenticated;
    }
    
    // Phương thức để loại bỏ client khỏi danh sách clients
    private void removeClient(Socket socket) {
        clients.remove(socket);
    }
    
    
    // Phương thức để lấy tin nhắn từ cơ sở dữ liệu cho một phòng cụ thể
    public static void getMessagesByRoomID(String roomID, DataOutputStream output) {
//        String connectionURL = "jdbc:sqlserver://localhost:1433;databaseName=Chat_Application;user=sa;password=cuong22052005;encrypt=true;trustServerCertificate=true;";
//        ArrayList<String> messages = new ArrayList<>();
        try (Connection connection = chat.app.JDBC.JDBCUtil.getConnection()) {
            String sql = "SELECT message FROM Message WHERE roomID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, roomID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
//                        messages.add(resultSet.getString("Message"));
                        // Gửi dữ liệu về client
                        try {
//                            JSONArray jsonArray = new JSONArray(messages);
                            output.writeUTF(resultSet.getString("message"));
                            output.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
