package services;

import dao.BillDao;
import models.Bill;
import java.sql.SQLException;
import java.util.List;

public class BillService {
    private BillDao dao = new BillDao();

    public double calc(int units) {
        // Existing logic (e.g., $0.10 per unit)
        return units * 0.10;
    }

    public boolean save(Bill b) throws SQLException {
        return dao.saveB(b);
    }

    public List<Bill> getBillsByCustomer(int customerId) throws SQLException {
        return dao.getBillsByCustomer(customerId);
    }
}