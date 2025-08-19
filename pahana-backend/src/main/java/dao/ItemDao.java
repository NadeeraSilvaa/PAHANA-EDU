package dao;

import models.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemDao {
    private Connection conn = DatabaseConnection.getConn();

    public boolean addIt(Item it) throws SQLException {
        String query = "INSERT INTO items (name, price) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, it.getItemName());
        stmt.setDouble(2, it.getItemPrice());
        return stmt.executeUpdate() > 0;
    }

    public boolean updateIt(Item it) throws SQLException {
        String query = "UPDATE items SET name=?, price=? WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, it.getItemName());
        stmt.setDouble(2, it.getItemPrice());
        stmt.setInt(3, it.getItemId());
        return stmt.executeUpdate() > 0;
    }

    public boolean deleteIt(int id) throws SQLException {
        String query = "DELETE FROM items WHERE id=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        return stmt.executeUpdate() > 0;
    }
}