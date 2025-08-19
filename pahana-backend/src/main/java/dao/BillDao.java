package dao;

import models.Bill;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BillDao {
    private Connection conn = DatabaseConnection.getConn();

    public boolean saveB(Bill b) throws SQLException {
        String query = "INSERT INTO bills (customer_id, total) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, b.getCustomerId());
        stmt.setDouble(2, b.getBillAmount());
        return stmt.executeUpdate() > 0;
    }
}