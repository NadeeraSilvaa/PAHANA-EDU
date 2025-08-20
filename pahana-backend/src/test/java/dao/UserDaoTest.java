package dao;

import models.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoTest {

    private static UserDao userDao;
    private static final String TEST_USERNAME = "admin";
    private static final String TEST_PASSWORD = "pass123";

    @BeforeAll
    static void setup() throws SQLException {
        userDao = new UserDao();
        // Insert test user
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            stmt.setString(1, TEST_USERNAME);
            stmt.setString(2, TEST_PASSWORD);
            stmt.setString(3, "admin");
            stmt.executeUpdate();
        }
    }

    @Test
    @Order(1)
    void testGetU_Success() throws SQLException {
        User user = userDao.getU(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(user, "User should be found");
        assertEquals(TEST_USERNAME, user.getUserName());
        assertEquals("admin", user.getRole());
    }

    @Test
    @Order(2)
    void testGetU_Fail() throws SQLException {
        User user = userDao.getU("wronguser", "wrongpass");
        assertNull(user, "User should not be found with invalid credentials");
    }

    @AfterAll
    static void cleanup() throws SQLException {
        // Remove test user
        try (Connection conn = DatabaseConnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM users WHERE username = ?")) {
            stmt.setString(1, TEST_USERNAME);
            stmt.executeUpdate();
        }
    }
}
