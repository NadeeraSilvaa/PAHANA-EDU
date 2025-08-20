package dao;

import models.Bill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDao {

    // ✅ Save Bill
    public boolean saveB(Bill b) throws SQLException {
        String sql = "INSERT INTO bills (customer_id, bill_amount, bill_date) VALUES (?, ?, NOW())";
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, b.getCustomerId());
            stmt.setDouble(2, b.getBillAmount());

            return stmt.executeUpdate() > 0;
        }
    }

    // ✅ Get Bills by Customer
    public List<Bill> getBillsByCustomer(int customerId) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Bill bill = new Bill();
                    bill.setId(rs.getInt("id"));
                    bill.setCustomerId(rs.getInt("customer_id"));
                    bill.setBillAmount(rs.getDouble("bill_amount"));
                    bill.setCreatedAt(rs.getTimestamp("bill_date"));
                    bills.add(bill);
                }
            }
        }
        return bills;
    }
}
