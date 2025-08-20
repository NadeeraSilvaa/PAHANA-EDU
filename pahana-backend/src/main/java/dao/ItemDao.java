package dao;

import models.Item;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {
    public boolean addIt(Item it) throws SQLException {
        String sql = "INSERT INTO items (name, price, author, image_url, category) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, it.getItemName());
            stmt.setDouble(2, it.getItemPrice());
            stmt.setString(3, it.getAuthor());
            stmt.setString(4, it.getImageUrl());
            stmt.setString(5, it.getCategory());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateIt(Item it) throws SQLException {
        String sql = "UPDATE items SET name=?, price=?, author=?, image_url=?, category=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, it.getItemName());
            stmt.setDouble(2, it.getItemPrice());
            stmt.setString(3, it.getAuthor());
            stmt.setString(4, it.getImageUrl());
            stmt.setString(5, it.getCategory());
            stmt.setInt(6, it.getItemId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteIt(int id) throws SQLException {
        String sql = "DELETE FROM items WHERE id=?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Item> searchByName(String name) throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE name LIKE ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setItemId(rs.getInt("id"));
                    item.setItemName(rs.getString("name"));
                    item.setAuthor(rs.getString("author"));
                    item.setItemPrice(rs.getDouble("price"));
                    item.setImageUrl(rs.getString("image_url"));
                    item.setCategory(rs.getString("category"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    public List<Item> getAll() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Item item = new Item();
                item.setItemId(rs.getInt("id"));
                item.setItemName(rs.getString("name"));
                item.setAuthor(rs.getString("author"));
                item.setItemPrice(rs.getDouble("price"));
                item.setImageUrl(rs.getString("image_url"));
                item.setCategory(rs.getString("category"));
                items.add(item);
            }
        }
        return items;
    }

    public Item getById(int id) throws SQLException {
        String sql = "SELECT * FROM items WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item();
                    item.setItemId(rs.getInt("id"));
                    item.setItemName(rs.getString("name"));
                    item.setAuthor(rs.getString("author"));
                    item.setItemPrice(rs.getDouble("price"));
                    item.setImageUrl(rs.getString("image_url"));
                    item.setCategory(rs.getString("category"));
                    return item;
                }
                return null;
            }
        }
    }
}