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

public class GetCustomerController extends HttpServlet {
    private CustomerService service = new CustomerService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            int accountNumber = Integer.parseInt(req.getParameter("accountNumber"));
            Customer customer = service.get(accountNumber);
            if (customer != null) {
                json.put("ok", true);
                JSONObject custJson = new JSONObject();
                custJson.put("accountNumber", customer.getAccountNumber());
                custJson.put("name", customer.getName());
                custJson.put("address", customer.getAddress());
                custJson.put("phone", customer.getPhone());
                json.put("customer", custJson);
            } else {
                json.put("ok", false);
                json.put("message", "Customer not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("ok", false);
            json.put("message", "Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            json.put("ok", false);
            json.put("message", "Invalid account number");
        }
        out.print(json);
    }
}