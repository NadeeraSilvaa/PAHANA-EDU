package controller;

import models.Customer;
import org.json.JSONObject;
import services.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class AddCustomerController extends HttpServlet {
    private CustomerService service = new CustomerService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String addr = req.getParameter("address");
        String phone = req.getParameter("phone");

        Customer cust = new Customer();
        cust.setName(name);
        cust.setAddress(addr);
        cust.setPhone(phone);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            boolean ok = service.add(cust);
            json.put("ok", ok);
            if (!ok) {
                json.put("message", "Failed to add customer (maybe duplicate or DB error).");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("ok", false);
            json.put("message", e.getMessage());
        }

        out.print(json);
    }
}