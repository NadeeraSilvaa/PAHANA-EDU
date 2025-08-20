package controller;

import org.json.JSONObject;
import services.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class DeleteCustomerController extends HttpServlet {
    private CustomerService service = new CustomerService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int accNum = Integer.parseInt(req.getParameter("accountNumber"));
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            boolean ok = service.delete(accNum);
            json.put("ok", ok);
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("error", "Failed to delete customer");
        }
        out.print(json);
    }
}