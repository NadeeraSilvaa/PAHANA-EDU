package controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import org.json.JSONObject;
import services.UserService;

public class LoginController extends HttpServlet {
    private UserService service = new UserService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        String uName = req.getParameter("username");
        String uPass = req.getParameter("password");

        if (uName != null && uPass != null && !uName.isEmpty() && !uPass.isEmpty()) {
            try {
                boolean ok = service.logIn(uName, uPass);
                json.put("ok", ok);
            } catch (SQLException e) {
                e.printStackTrace();
                json.put("ok", false);
            } finally {
                dao.DatabaseConnection.closeConn(); // Close connection after use
            }
        } else {
            json.put("ok", false);
        }

        out.print(json);
        out.flush();
    }
}