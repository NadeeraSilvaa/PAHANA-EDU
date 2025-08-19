package services;

import dao.CustomerDao;
import factory.DaoFactory;
import models.Customer;
import validators.CustomerValidator;

import java.sql.SQLException;

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

    public Customer get(int accNum) throws SQLException {
        return dao.getCust(accNum);
    }
}