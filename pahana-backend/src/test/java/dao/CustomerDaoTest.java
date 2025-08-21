package dao;

import models.Customer;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerDaoTest {

    private static CustomerDao dao;
    private static int testAccNumber;

    @BeforeAll
    static void setup() throws SQLException {
        dao = new CustomerDao();
        // Insert a test customer
        Customer cust = new Customer();
        cust.setName("Test Customer");
        cust.setAddress("123 Test St");
        cust.setPhone("0123456789");

        dao.addCust(cust);

        // Retrieve account number of last inserted customer
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT account_number FROM customers WHERE name=? ORDER BY account_number DESC LIMIT 1")) {
            stmt.setString(1, cust.getName());
            var rs = stmt.executeQuery();
            if (rs.next()) testAccNumber = rs.getInt("account_number");
        }
    }

    @Test
    @Order(1)
    void testGetCust() throws SQLException {
        Customer cust = dao.getCust(testAccNumber);
        assertNotNull(cust, "Customer should be found");
        assertEquals("Test Customer", cust.getName());
    }

    @Test
    @Order(2)
    void testEditCust() throws SQLException {
        Customer cust = dao.getCust(testAccNumber);
        cust.setName("Updated Customer");
        boolean updated = dao.editCust(cust);
        assertTrue(updated, "Customer should be updated");

        Customer updatedCust = dao.getCust(testAccNumber);
        assertEquals("Updated Customer", updatedCust.getName());
    }

    @Test
    @Order(3)
    void testUpdateUnits() throws SQLException {
        boolean updated = dao.updateUnits(testAccNumber, 5);
        assertTrue(updated, "Units should be updated");

        Customer cust = dao.getCust(testAccNumber);
        assertEquals(5, cust.getUnitsConsumed());
    }

    @Test
    @Order(4)
    void testGetAll() throws SQLException {
        List<Customer> customers = dao.getAll();
        assertFalse(customers.isEmpty(), "Customers list should not be empty");
    }

    @Test
    @Order(5)
    void testDelete() throws SQLException {
        boolean deleted = dao.delete(testAccNumber);
        assertTrue(deleted, "Customer should be deleted");

        Customer cust = dao.getCust(testAccNumber);
        assertNull(cust, "Customer should no longer exist");
    }
}
