/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat.app.JDBC;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

/**
 *
 * @author ACER
 */
public class JDBCUtil {
//    public static Connection getConnection() {
//        var server = "LAPTOP-67Q6A3IT\\SQLEXPRESS";
//        var user = "sa";
//        var password = "cuong22052005";
//        var db = "Chat_Application";
//        var port = 1433;
//        SQLServerDataSource ds = new SQLServerDataSource();
//
//        ds.setUser(user);
//        ds.setPassword(password);
//        ds.setDatabaseName(db);
//        ds.setServerName(server);
//        ds.setPortNumber(port);
//        ds.setEncrypt(false);
//        
//        try (Connection conn = ds.getConnection()) {
//            System.out.println("Kết nối thành công");
//            System.out.println(conn.getCatalog());
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
    
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Chat_Application;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "cuong22052005";

    private static Connection connection;

    private JDBCUtil() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException("SQLServer JDBC Driver not found");
            }
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
