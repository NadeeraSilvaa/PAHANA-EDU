package services;

import dao.CustomerDao;
import factory.DaoFactory;
import models.Customer;
import validators.CustomerValidator;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private CustomerDao dao = DaoFactory.getCustDao();

    public boolean add(Customer cust) throws SQLException {
        if (!CustomerValidator.isValid(cust)) {
            return false;
        }
        return dao.addCust(cust);
    }

    public boolean edit(Customer cust) throws SQLException {
        if (!CustomerValidator.isValid(cust)) {
            return false;
        }
        return dao.editCust(cust);
    }

    public boolean updateUnits(int accountNumber, int additionalUnits) throws SQLException {
        return dao.updateUnits(accountNumber, additionalUnits);
    }

    public Customer get(int accNum) throws SQLException {
        return dao.getCust(accNum);
    }

    public boolean delete(int accNum) throws SQLException {
        return dao.delete(accNum);
    }

    public List<Customer> getAll() throws SQLException {
        return dao.getAll();
    }
}