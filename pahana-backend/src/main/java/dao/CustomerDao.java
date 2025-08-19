package dao;

import models.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDao {

    // ✅ Add Customer
    public boolean addCust(Customer cust) throws SQLException {
        String sql = "INSERT INTO customers (name, address, phone, units) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cust.getName());
            stmt.setString(2, cust.getAddress());
            stmt.setString(3, cust.getPhone());
            stmt.setInt(4, cust.getUnitsConsumed());

            return stmt.executeUpdate() > 0;
        }
    }

    // ✅ Edit Customer
    public boolean editCust(Customer cust) throws SQLException {
        String sql = "UPDATE customers SET name=?, address=?, phone=?, units=? WHERE account_number=?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cust.getName());
            stmt.setString(2, cust.getAddress());
            stmt.setString(3, cust.getPhone());
            stmt.setInt(4, cust.getUnitsConsumed());
            stmt.setInt(5, cust.getAccountNumber());

            return stmt.executeUpdate() > 0;
        }
    }

    // ✅ Get Customer by Account Number
    public Customer getCust(int accNum) throws SQLException {
        String sql = "SELECT * FROM customers WHERE account_number=?";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accNum);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer cust = new Customer();
                    cust.setAccountNumber(rs.getInt("account_number"));
                    cust.setName(rs.getString("name"));
                    cust.setAddress(rs.getString("address"));
                    cust.setPhone(rs.getString("phone"));
                    cust.setUnitsConsumed(rs.getInt("units"));
                    return cust;
                }
            }
        }
        return null; // not found
    }
}
