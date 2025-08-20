package dao;

import models.Bill;
import models.Customer;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BillDaoTest {

    private static BillDao billDao;
    private static CustomerDao customerDao;
    private static int testCustomerId;

    @BeforeAll
    static void setup() throws SQLException {
        billDao = new BillDao();
        customerDao = new CustomerDao();

        // Add a test customer
        Customer cust = new Customer();
        cust.setName("Bill Test Customer");
        cust.setAddress("123 Test St");
        cust.setPhone("0123456789");
        customerDao.addCust(cust);

        // Retrieve inserted customer's account_number
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT account_number FROM customers WHERE name=? ORDER BY account_number DESC LIMIT 1")) {
            stmt.setString(1, cust.getName());
            var rs = stmt.executeQuery();
            if (rs.next()) testCustomerId = rs.getInt("account_number");
        }
    }

    @Test
    @Order(1)
    void testSaveB() throws SQLException {
        Bill bill = new Bill();
        bill.setCustomerId(testCustomerId);
        bill.setBillAmount(150.0);

        boolean saved = billDao.saveB(bill);
        assertTrue(saved, "Bill should be saved successfully");
    }

    @Test
    @Order(2)
    void testGetBillsByCustomer() throws SQLException {
        List<Bill> bills = billDao.getBillsByCustomer(testCustomerId);
        assertFalse(bills.isEmpty(), "Bills list should not be empty");

        Bill bill = bills.get(0);
        assertEquals(testCustomerId, bill.getCustomerId());
        assertEquals(150.0, bill.getBillAmount());
        assertNotNull(bill.getCreatedAt(), "Bill date should not be null");
    }

    @AfterAll
    static void cleanup() throws SQLException {
        // Delete bills of test customer
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM bills WHERE customer_id=?")) {
            stmt.setInt(1, testCustomerId);
            stmt.executeUpdate();
        }

        // Delete test customer
        customerDao.delete(testCustomerId);
    }
}
