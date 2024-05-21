package Server;

import Server.app.JDBC.JDBCUtil;
//import static Server.Server.input;
//import static Server.Server.output;
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
    DataInputStream input;
    DataOutputStream output;
    public static ArrayList<Socket> clients = new ArrayList<>();
    public static Map<String, List<ClientHandler>> rooms = new HashMap<>();
    private static Map<ClientHandler, List<String>> clientRooms = new HashMap<>();
//    static DataOutputStream output;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.input = new DataInputStream(socket.getInputStream()); // Tạo input stream cho client handler
            this.output = new DataOutputStream(socket.getOutputStream()); // Tạo output stream cho client handler
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try /*(DataInputStream input = new DataInputStream(socket.getInputStream()); DataOutputStream output = new DataOutputStream(socket.getOutputStream()))*/{
            
//            String roomId = input.readUTF();
//            this.roomId = Server.roomID;
//            getMessagesByRoomID(roomId, Server.output);
            
            while (true) { 
                String jsonString  = input.readUTF();
                
                JSONObject receivedJson = new JSONObject(jsonString );
                String type = (String) receivedJson.get("type");
                
                if (type.equals("send_message")) {
                    String roomId2 = receivedJson.getString("roomId");
                    String message = receivedJson.getString("message");
                    
                    JSONObject json = new JSONObject();
//                    json.put("type", "send_message");
                    json.put("message", message);
                    sendMessageToRoom(roomId2, json);
                    saveMessageToDatabase(receivedJson);
                } else if (type.equals("create_room")){
                    saveDataRoomToDatabase(receivedJson);
                } else if (type.equals("create_account")) {
                    saveAccountToDatabase(receivedJson);
                } else if (type.equals("login")) {
                    checkAccount(receivedJson);
                } else if (type.equals("history_message")) {
                    roomId = receivedJson.getString("roomid");
                    getMessagesByRoomID(roomId, output, this);
                    removeAllRoomsForClient(this);
                    addClientToRoom(roomId, this);
                } else if (type.equals("ConfirmPasswordRoom")) {
                    confirmPasswordRoom(receivedJson);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            removeClientFromRoom(roomId, this);
        }
    }
    
    public void confirmPasswordRoom(JSONObject json) {
        boolean isAuthenticated = false;
        try (Connection connection = JDBCUtil.getConnection()) {
            // Chuẩn bị câu lệnh SQL để kiểm tra mật khẩu và roomID
            String sql = "SELECT 1 FROM roomId WHERE roomId = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Thiết lập giá trị cho các tham số từ JSON
                preparedStatement.setString(1, json.getString("roomId"));
                preparedStatement.setString(2, json.getString("password"));

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    // Kiểm tra nếu có bản ghi nào được trả về
                    isAuthenticated = rs.next();
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("type", "confirmPassword");

                    if (isAuthenticated) {
                        jsonResponse.put("status", "success");
                        output.writeUTF(jsonResponse.toString());
                        output.flush();
                        System.out.println("Password is correct.");
                    } else {
                        jsonResponse.put("status", "failed");
                        output.writeUTF(jsonResponse.toString());
                        output.flush();
                        System.out.println("Invalid password.");
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            try {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("type", "confirmPassword");
                jsonResponse.put("status", "error");
                output.writeUTF(jsonResponse.toString());
                output.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
    
    public static synchronized void removeAllRoomsForClient(ClientHandler clientHandler) {
        List<String> roomsForClient = clientRooms.get(clientHandler);
        if (roomsForClient != null) {
            for (String roomId : roomsForClient) {
                removeClientFromRoom(roomId, clientHandler);
            }
            clientRooms.remove(clientHandler);
        }
    }

    public static synchronized void addClientToRoom(String roomId, ClientHandler clientHandler) {
        List<ClientHandler> clientsInRoom = rooms.get(roomId);
        if (clientsInRoom == null) {
            clientsInRoom = new ArrayList<>();
            rooms.put(roomId, clientsInRoom);
        }

        // Kiểm tra xem phòng đã có client handler nào chưa
        if (!clientsInRoom.contains(clientHandler)) {
            clientsInRoom.add(clientHandler);

            // Cập nhật clientRooms
            List<String> roomsForClient = clientRooms.get(clientHandler);
            if (roomsForClient == null) {
                roomsForClient = new ArrayList<>();
                clientRooms.put(clientHandler, roomsForClient);
            }
            if (!roomsForClient.contains(roomId)) {
                roomsForClient.add(roomId);
            }
        }
    }
    
    // Trong phương thức addClientToRoom
//    public static synchronized void addClientToRoom(String roomId, ClientHandler clientHandler) {
//        List<ClientHandler> clientsInRoom = rooms.get(roomId);
//        if (clientsInRoom == null) {
//            clientsInRoom = new ArrayList<>();
//            rooms.put(roomId, clientsInRoom);
//        }
//
//        // Kiểm tra xem phòng đã có client handler nào chưa
//        if (!clientsInRoom.contains(clientHandler)) {
//            clientsInRoom.add(clientHandler);
//        }
//    }
    
    public static synchronized void sendMessageToRoom(String roomId, JSONObject message) {
        List<ClientHandler> clientsInRoom = rooms.get(roomId);
        if (clientsInRoom != null) {
            for (ClientHandler client : clientsInRoom) {
                try {
                    JSONObject jsonMess = new JSONObject();
                    jsonMess.put("type", "send_message");
                    jsonMess.put("message", message.getString("message"));
                    client.output.writeUTF(jsonMess.toString());
                    client.output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // Phương thức để lưu tin nhắn từ JSON vào cơ sở dữ liệu
    public void saveMessageToDatabase(JSONObject json) {
        try (Connection connection = JDBCUtil.getConnection()) {
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
        try (Connection connection = JDBCUtil.getConnection()) {
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
                
                JSONObject json1 = new JSONObject();
                json1.put("type", "create_account");
                json1.put("status", "success");
                
                try {
                    output.writeUTF(json1.toString());
                output.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();e.printStackTrace();
            // Gửi trạng thái lỗi về client
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("type", "create_account");
            jsonResponse.put("status", "failed");

            try {
                output.writeUTF(jsonResponse.toString());
                output.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    // Phương thức để lưu tin nhắn từ JSON vào cơ sở dữ liệu
    public void saveDataRoomToDatabase(JSONObject json) {
        try (Connection connection = JDBCUtil.getConnection()) {
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
                
                JSONObject json1 = new JSONObject();
                json1.put("type", "create_room");
                json1.put("status", "success");
                
                try {
                    output.writeUTF(json1.toString());
                    output.flush();
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JSONObject json1 = new JSONObject();
            json1.put("type", "create_room");
            json1.put("status", "failed");
            try {
                output.writeUTF(json1.toString());
                output.flush();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void checkAccount(JSONObject json) {
        boolean isAuthenticated = false;
        try (Connection connection = JDBCUtil.getConnection()) {
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
                        try {
                            JSONObject jsonAccounnt = new JSONObject();
                            String name = getName(json.getString("email"));
                            jsonAccounnt.put("type", "login");
                            jsonAccounnt.put("status", "success");
                            jsonAccounnt.put("name", name);
                            output.writeUTF(jsonAccounnt.toString());
                            output.flush();
                            System.out.println("Đã gởi log_in");
                        } catch (IOException ex) {
                            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            JSONObject jsonAccounnt = new JSONObject();
                            jsonAccounnt.put("type", "login");
                            jsonAccounnt.put("status", "failed");
                            output.writeUTF(jsonAccounnt.toString());
                            output.flush();
                            System.out.println("Đã gởi log_in fail");
                        } catch (IOException ex) {
                            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        return isAuthenticated;
    }
    
    private String getName (String email) {
        String name = null;
        try (Connection connection = JDBCUtil.getConnection()) {
            String sql = "SELECT name FROM users WHERE email=?";
            try (PreparedStatement st = connection.prepareStatement(sql)) {
                st.setString(1, email);
                
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        name = rs.getString("name");
                        System.out.println("Tên: " + name);
                    }
                } catch (Exception e) {
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        return name;
    }
    
    // Phương thức để loại bỏ client khỏi danh sách clients
    private void removeClient(Socket socket) {
        clients.remove(socket);
    }
    
    // Trong phương thức getMessagesByRoomID
    public static void getMessagesByRoomID(String roomID, DataOutputStream output, ClientHandler clientHandler) {
        // Code cũ
        try (Connection connection = JDBCUtil.getConnection()) {
            String sql = "SELECT message FROM Message WHERE roomID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, roomID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        JSONObject json = new JSONObject();
                        json.put("type", "history_message");
                        json.put("message", resultSet.getString("message"));
                        try {
                            // Gửi tin nhắn lịch sử cho 'output' của client handler hiện tại
                            output.writeUTF(json.toString());
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


//package Server;
//
//import Server.app.JDBC.JDBCUtil;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.json.JSONObject;
//
//public class ClientHandler implements Runnable {
//    Socket socket;
//    String roomId;
//    DataInputStream input;
//    DataOutputStream output; // Thêm DataOutputStream cho mỗi client handler
//    public static ArrayList<Socket> clients = new ArrayList<>();
//    public static Map<String, List<ClientHandler>> rooms = new HashMap<>();
//
//    public ClientHandler(Socket socket) {
//        this.socket = socket;
//        try {
//            this.input = new DataInputStream(socket.getInputStream()); // Tạo input stream cho client handler
//            this.output = new DataOutputStream(socket.getOutputStream()); // Tạo output stream cho client handler
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (true) { 
//                String jsonString = input.readUTF(); // Đọc dữ liệu từ client
//                JSONObject receivedJson = new JSONObject(jsonString);
//                String type = receivedJson.getString("type");
//
//                if (type.equals("send_message")) {
//                    String roomId2 = receivedJson.getString("roomId");
//                    String message = receivedJson.getString("message");
//                    JSONObject json = new JSONObject();
//                    json.put("message", message);
//                    sendMessageToRoom(roomId2, json); // Gửi tin nhắn đến phòng
//                    saveMessageToDatabase(receivedJson); // Lưu tin nhắn vào cơ sở dữ liệu
//                } else if (type.equals("create_room")) {
//                    saveDataRoomToDatabase(receivedJson); // Tạo phòng mới
//                } else if (type.equals("create_account")) {
//                    saveAccountToDatabase(receivedJson); // Tạo tài khoản mới
//                } else if (type.equals("login")) {
//                    checkAccount(receivedJson); // Kiểm tra đăng nhập
//                } else if (type.equals("history_message")) {
//                    roomId = receivedJson.getString("roomid");
//                    getMessagesByRoomID(roomId, this.output); // Gửi lịch sử tin nhắn cho client hiện tại
//                    addClientToRoom(roomId, this); // Thêm client vào phòng
//                } 
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            removeClientFromRoom(roomId, this); // Xóa client khỏi phòng khi kết thúc
//        }
//    }
//
//    public static synchronized void removeClientFromRoom(String roomId, ClientHandler clientHandler) {
//        List<ClientHandler> clientsInRoom = rooms.get(roomId);
//        if (clientsInRoom != null) {
//            clientsInRoom.remove(clientHandler); // Xóa client khỏi danh sách phòng
//            if (clientsInRoom.isEmpty()) {
//                rooms.remove(roomId); // Xóa phòng nếu không còn client nào
//            }
//        } else {
//            System.out.println("null");
//        }
//    }
//
//    public static synchronized void sendMessageToRoom(String roomId, JSONObject message) {
//        List<ClientHandler> clientsInRoom = rooms.get(roomId);
//        if (clientsInRoom != null) {
//            for (ClientHandler client : clientsInRoom) {
//                try {
//                    JSONObject jsonMess = new JSONObject();
//                    jsonMess.put("type", "send_message");
//                    jsonMess.put("message", message.getString("message"));
//                    client.output.writeUTF(jsonMess.toString()); // Gửi tin nhắn đến client
//                    client.output.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static synchronized void addClientToRoom(String roomId, ClientHandler clientHandler) {
//        List<ClientHandler> clientsInRoom = rooms.get(roomId);
//        if (clientsInRoom == null) {
//            clientsInRoom = new ArrayList<>();
//            rooms.put(roomId, clientsInRoom);
//        }
//        clientsInRoom.add(clientHandler); // Thêm client vào phòng
//    }
//
//    public void saveMessageToDatabase(JSONObject json) {
//        try (Connection connection = JDBCUtil.getConnection()) {
//            String sql = "INSERT INTO Message (RoomID, Message) VALUES (?, ?)";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                preparedStatement.setString(1, json.getString("roomId"));
//                preparedStatement.setString(2, json.getString("message"));
//                preparedStatement.executeUpdate();
//                System.out.println("Message saved to database successfully!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void saveAccountToDatabase(JSONObject json) throws IOException {
//        try (Connection connection = JDBCUtil.getConnection()) {
//            String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                preparedStatement.setString(1, json.getString("name"));
//                preparedStatement.setString(2, json.getString("email"));
//                preparedStatement.setString(3, json.getString("password"));
//                preparedStatement.executeUpdate();
//                System.out.println("Account saved to database successfully!");
//                JSONObject json1 = new JSONObject();
//                json1.put("type", "create_account");
//                json1.put("status", "success");
//                output.writeUTF(json1.toString()); // Gửi kết quả tạo tài khoản thành công
//                output.flush();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JSONObject jsonResponse = new JSONObject();
//            jsonResponse.put("type", "create_account");
//            jsonResponse.put("status", "failed");
//            try {
//                output.writeUTF(jsonResponse.toString()); // Gửi kết quả tạo tài khoản thất bại
//                output.flush();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    public void saveDataRoomToDatabase(JSONObject json) throws IOException {
//        try (Connection connection = JDBCUtil.getConnection()) {
//            String sql = "INSERT INTO RoomID (RoomID, PassWord) VALUES (?, ?)";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                preparedStatement.setString(1, json.getString("roomId"));
//                preparedStatement.setString(2, json.getString("password"));
//                preparedStatement.executeUpdate();
//                System.out.println("Room saved to database successfully!");
//                JSONObject json1 = new JSONObject();
//                json1.put("type", "create_room");
//                json1.put("status", "success");
//                output.writeUTF(json1.toString()); // Gửi kết quả tạo phòng thành công
//                output.flush();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JSONObject json1 = new JSONObject();
//            json1.put("type", "create_room");
//            json1.put("status", "failed");
//            try {
//                output.writeUTF(json1.toString()); // Gửi kết quả tạo phòng thất bại
//                output.flush();
//            } catch (IOException ex) {
//                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//    public void checkAccount(JSONObject json) {
//        boolean isAuthenticated = false;
//        try (Connection connection = JDBCUtil.getConnection()) {
//            String sql = "SELECT 1 FROM users WHERE email = ? AND password = ?";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                preparedStatement.setString(1, json.getString("email"));
//                preparedStatement.setString(2, json.getString("password"));
//                try (ResultSet rs = preparedStatement.executeQuery()) {
//                    isAuthenticated = rs.next();
//                    JSONObject jsonAccounnt = new JSONObject();
//                    if (isAuthenticated) {
//                        String name = getName(json.getString("email"));
//                        jsonAccounnt.put("type", "login");
//                        jsonAccounnt.put("status", "success");
//                        jsonAccounnt.put("name", name);
//                    } else {
//                        jsonAccounnt.put("type", "login");
//                        jsonAccounnt.put("status", "failed");
//                    }
//                    output.writeUTF(jsonAccounnt.toString()); // Gửi kết quả đăng nhập
//                    output.flush();
//                }
//            }
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getName(String email) {
//        String name = null;
//        try (Connection connection = JDBCUtil.getConnection()) {
//            String sql = "SELECT name FROM users WHERE email=?";
//            try (PreparedStatement st = connection.prepareStatement(sql)) {
//                st.setString(1, email);
//                try (ResultSet rs = st.executeQuery()) {
//                    if (rs.next()) {
//                        name = rs.getString("name");
//                        System.out.println("Tên: " + name);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return name;
//    }
//
//    private void removeClient(Socket socket) {
//        clients.remove(socket);
//    }
//
//    public static void getMessagesByRoomID(String roomID, DataOutputStream output) {
//        try (Connection connection = JDBCUtil.getConnection()) {
//            String sql = "SELECT message FROM Message WHERE roomID = ?";
//            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//                preparedStatement.setString(1, roomID);
//                try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                    while (resultSet.next()) {
//                        JSONObject json = new JSONObject();
//                        json.put("type", "history_message");
//                        json.put("message", resultSet.getString("message"));
//                        output.writeUTF(json.toString()); // Gửi từng tin nhắn lịch sử cho client
//                        output.flush();
//                    }
//                }
//            }
//        } catch (SQLException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
