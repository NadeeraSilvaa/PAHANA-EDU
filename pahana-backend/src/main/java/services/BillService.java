package services;

import dao.BillDao;
import models.Bill;
import java.sql.SQLException;
import java.util.List;

public class BillService {
    private BillDao dao = new BillDao();

    public boolean save(Bill b) throws SQLException {
        return dao.saveB(b);
    }

    public List<Bill> getBillsByCustomer(int customerId) throws SQLException {
        return dao.getBillsByCustomer(customerId);
    }
}