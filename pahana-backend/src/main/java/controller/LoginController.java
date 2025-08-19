package controller;

import org.json.JSONObject;
import services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class LoginController extends HttpServlet {
    private UserService service = new UserService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uName = req.getParameter("username");
        String uPass = req.getParameter("password");

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            boolean ok = service.logIn(uName, uPass);
            json.put("ok", ok);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.print(json);
    }
}