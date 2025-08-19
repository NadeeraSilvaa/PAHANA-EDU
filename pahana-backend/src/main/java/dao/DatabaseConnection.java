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

                // Localhost connection
                String url = "jdbc:mysql://localhost:3306/pahanaedu?useSSL=false&allowPublicKeyRetrieval=true";
                String username = "root";       // default XAMPP MySQL user
                String password = "123456"; // whatever you set in XAMPP

                conn = DriverManager.getConnection(url, username, password);
                System.out.println("✅ Connected to local MySQL");
            } catch (ClassNotFoundException e) {
                System.out.println("❌ MySQL Driver not found. Add mysql-connector-java.jar to classpath.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("❌ Database connection failed.");
                e.printStackTrace();
            }
        }
        return conn;
    }
}
