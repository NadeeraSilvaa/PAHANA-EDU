package services;

import dao.ItemDao;
import factory.DaoFactory;
import models.Item;
import validators.ItemValidator;

import java.sql.SQLException;

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
}