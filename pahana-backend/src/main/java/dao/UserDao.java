package dao;

import models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private Connection conn = DatabaseConnection.getConn();

    public User getU(String uName, String uPass) throws SQLException {
        String query = "SELECT * FROM users WHERE username=? AND password=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, uName);
        stmt.setString(2, uPass);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            User u = new User();
            u.setUserId(rs.getInt("id"));
            u.setUserName(rs.getString("username"));
            u.setUserPass(rs.getString("password"));
            return u;
        }
        return null;
    }
}