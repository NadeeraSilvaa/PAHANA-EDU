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

public class DisplayAccountController extends HttpServlet {
    private CustomerService service = new CustomerService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int accNum = Integer.parseInt(req.getParameter("accountNumber"));
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            Customer cust = service.get(accNum);
            if (cust != null) {
                json.put("name", cust.getName());
                json.put("address", cust.getAddress());
                json.put("phone", cust.getPhone());
                json.put("units", cust.getUnitsConsumed());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.print(json);
    }
}