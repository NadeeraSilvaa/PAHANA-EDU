package factory;

import dao.BillDao;
import dao.CustomerDao;
import dao.ItemDao;
import dao.UserDao;

public class DaoFactory {
    private static CustomerDao custDao;
    private static ItemDao itDao;
    private static BillDao bDao;
    private static UserDao uDao;

    public static CustomerDao getCustDao() {
        if (custDao == null) {
            custDao = new CustomerDao();
        }
        return custDao;
    }

    public static ItemDao getItDao() {
        if (itDao == null) {
            itDao = new ItemDao();
        }
        return itDao;
    }

    public static BillDao getBDao() {
        if (bDao == null) {
            bDao = new BillDao();
        }
        return bDao;
    }

    public static UserDao getUDao() {
        if (uDao == null) {
            uDao = new UserDao();
        }
        return uDao;
    }
}