package services;

import dao.ItemDao;
import factory.DaoFactory;
import models.Item;
import validators.ItemValidator;
import java.sql.SQLException;
import java.util.List;

public class ItemService {
    private ItemDao dao = DaoFactory.getItDao();

    public boolean add(Item it) throws SQLException {
        if (!ItemValidator.isValid(it)) {
            return false;
        }
        return dao.addIt(it);
    }

    public boolean update(Item it) throws SQLException {
        if (!ItemValidator.isValid(it)) {
            return false;
        }
        return dao.updateIt(it);
    }

    public boolean delete(int id) throws SQLException {
        return dao.deleteIt(id);
    }

    public List<Item> searchByName(String name) throws SQLException {
        return dao.searchByName(name);
    }

    public List<Item> getAll() throws SQLException {
        return dao.getAll();
    }

    public Item getById(int id) throws SQLException {
        return dao.getById(id);
    }
}