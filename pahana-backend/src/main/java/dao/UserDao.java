package dao;

import models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    // Get User by username + password
    public User getU(String uName, String uPass) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, uName);
            stmt.setString(2, uPass);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("id"));
                    u.setUserName(rs.getString("username"));
                    u.setUserPass(rs.getString("password"));
                    u.setRole(rs.getString("role"));
                    return u;
                }
            }
        }
        return null; // not found
    }
}