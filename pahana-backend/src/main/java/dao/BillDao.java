package dao;

import models.Bill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDao {
    private Connection conn = DatabaseConnection.getConn();

    public boolean saveB(Bill b) throws SQLException {
        String query = "INSERT INTO bills (customer_id, bill_amount, bill_date) VALUES (?, ?, NOW())";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, b.getCustomerId());
        stmt.setDouble(2, b.getBillAmount());
        return stmt.executeUpdate() > 0;
    }

    public List<Bill> getBillsByCustomer(int customerId) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM bills WHERE customer_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, customerId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Bill bill = new Bill();
            bill.setId(rs.getInt("id"));
            bill.setCustomerId(rs.getInt("customer_id"));
            bill.setBillAmount(rs.getDouble("bill_amount"));
            bill.setCreatedAt(rs.getTimestamp("bill_date"));
            bills.add(bill);
        }
        rs.close();
        stmt.close();
        return bills;
    }

}