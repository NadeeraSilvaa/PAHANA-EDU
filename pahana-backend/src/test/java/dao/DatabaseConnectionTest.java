package dao;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void testGetConn_NotNull() {
        try (Connection conn = DatabaseConnection.getConn()) {
            assertNotNull(conn, "Connection should not be null");
            assertFalse(conn.isClosed(), "Connection should be open");
        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetConn_ExecuteSimpleQuery() {
        try (Connection conn = DatabaseConnection.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1")) {

            assertTrue(rs.next(), "ResultSet should have at least one row");
            assertEquals(1, rs.getInt(1), "Query should return 1");

        } catch (SQLException e) {
            fail("SQLException thrown: " + e.getMessage());
        }
    }
}
