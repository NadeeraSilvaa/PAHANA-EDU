package services;

import dao.BillDao;
import factory.DaoFactory;
import models.Bill;
import java.sql.SQLException;

public class BillService {
    private BillDao dao = DaoFactory.getBDao();

    public double calc(int units) {
        double price = 10.0;  // Simple
        return units * price;
    }

    public boolean save(Bill b) throws SQLException {
        return dao.saveB(b);
    }
}