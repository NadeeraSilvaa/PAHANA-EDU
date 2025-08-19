package services;

import dao.UserDao;
import factory.DaoFactory;
import java.sql.SQLException;

public class UserService {
    private UserDao dao = DaoFactory.getUDao();

    public boolean logIn(String uName, String uPass) throws SQLException {
        return dao.getU(uName, uPass) != null;
    }
}