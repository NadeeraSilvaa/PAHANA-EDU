package dao;

import models.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDao {
    private Connection conn = DatabaseConnection.getConn();

    public boolean addCust(Customer cust) throws SQLException {
        String query = "INSERT INTO customers (name, address, phone, units) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, cust.getName());
        stmt.setString(2, cust.getAddress());
        stmt.setString(3, cust.getPhone());
        stmt.setInt(4, cust.getUnitsConsumed());
        return stmt.executeUpdate() > 0;
    }

    public boolean editCust(Customer cust) throws SQLException {
        String query = "UPDATE customers SET name=?, address=?, phone=?, units=? WHERE account_number=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, cust.getName());
        stmt.setString(2, cust.getAddress());
        stmt.setString(3, cust.getPhone());
        stmt.setInt(4, cust.getUnitsConsumed());
        stmt.setInt(5, cust.getAccountNumber());
        return stmt.executeUpdate() > 0;
    }

    public Customer getCust(int accNum) throws SQLException {
        String query = "SELECT * FROM customers WHERE account_number=?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, accNum);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Customer cust = new Customer();
            cust.setAccountNumber(rs.getInt("account_number"));
            cust.setName(rs.getString("name"));
            cust.setAddress(rs.getString("address"));
            cust.setPhone(rs.getString("phone"));
            cust.setUnitsConsumed(rs.getInt("units"));
            return cust;
        }
        return null;
    }
}