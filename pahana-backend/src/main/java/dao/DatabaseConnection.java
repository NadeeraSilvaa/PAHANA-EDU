package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection conn;

    public static Connection getConn() {
        if (conn == null) {
            try {
                // Load MySQL driver (optional in recent Java versions)
                Class.forName("com.mysql.cj.jdbc.Driver");

                String url = "jdbc:mysql://localhost:3306/pahanaedu?useSSL=false&allowPublicKeyRetrieval=true";
                String username = "root";
                String password = "123456";

                conn = DriverManager.getConnection(url, username, password);
                System.out.println("✅ Connected to local MySQL");
            } catch (ClassNotFoundException e) {
                System.out.println("❌ MySQL Driver not found. Add mysql-connector-java.jar to classpath.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("❌ Database connection failed.");
                e.printStackTrace();
            }
        } else {
            try {
                if (conn.isClosed()) {
                    conn = null; // Reset connection if closed
                    System.out.println("⚠️ Previous connection was closed. Reconnecting...");
                    return getConn(); // Recursively get a new connection
                }
            } catch (SQLException e) {
                System.out.println("❌ Error checking connection status. Reconnecting...");
                e.printStackTrace();
                conn = null; // Reset on error
                return getConn(); // Try to reconnect
            }
        }
        return conn;
    }

    public static void closeConn() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("✅ Database connection closed.");
            } catch (SQLException e) {
                System.out.println("❌ Failed to close database connection.");
                e.printStackTrace();
            } finally {
                conn = null; // Ensure conn is reset after closing
            }
        }
    }
}