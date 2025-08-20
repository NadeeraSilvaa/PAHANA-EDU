package dao;

import models.Item;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemDaoTest {

    private static ItemDao itemDao;
    private static int testItemId;

    @BeforeAll
    static void setup() {
        itemDao = new ItemDao();
    }

    @Test
    @Order(1)
    void testAddIt() throws SQLException {
        Item item = new Item();
        item.setItemName("JUnit Test Book");
        item.setAuthor("Test Author");
        item.setItemPrice(19.99);
        item.setCategory("Test Category");
        item.setImageUrl("test.jpg");

        boolean added = itemDao.addIt(item);
        assertTrue(added, "Item should be added");

        // Retrieve ID for further tests
        List<Item> items = itemDao.searchByName("JUnit Test Book");
        assertFalse(items.isEmpty(), "Item list should not be empty");
        testItemId = items.get(0).getItemId();
    }

    @Test
    @Order(2)
    void testGetById() throws SQLException {
        Item item = itemDao.getById(testItemId);
        assertNotNull(item, "Item should exist");
        assertEquals("JUnit Test Book", item.getItemName());
    }

    @Test
    @Order(3)
    void testUpdateIt() throws SQLException {
        Item item = itemDao.getById(testItemId);
        item.setItemPrice(29.99);
        boolean updated = itemDao.updateIt(item);
        assertTrue(updated, "Item should be updated");

        Item updatedItem = itemDao.getById(testItemId);
        assertEquals(29.99, updatedItem.getItemPrice());
    }

    @Test
    @Order(4)
    void testSearchByName() throws SQLException {
        List<Item> items = itemDao.searchByName("JUnit Test");
        assertFalse(items.isEmpty(), "Search result should not be empty");
    }

    @Test
    @Order(5)
    void testGetAll() throws SQLException {
        List<Item> items = itemDao.getAll();
        assertTrue(items.size() > 0, "There should be at least one item in the database");
    }

    @Test
    @Order(6)
    void testDeleteIt() throws SQLException {
        boolean deleted = itemDao.deleteIt(testItemId);
        assertTrue(deleted, "Item should be deleted");

        Item item = itemDao.getById(testItemId);
        assertNull(item, "Deleted item should not exist");
    }
}
