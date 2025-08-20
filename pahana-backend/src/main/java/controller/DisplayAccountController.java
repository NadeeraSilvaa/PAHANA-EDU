package controller;

import models.Customer;
import org.json.JSONArray;
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
    CustomerService service = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            if ("all".equals(action)) {
                // Fetch all customers
                JSONArray jsonArray = new JSONArray();
                for (Customer cust : service.getAll()) {
                    JSONObject json = new JSONObject();
                    json.put("accountNumber", cust.getAccountNumber());
                    json.put("name", cust.getName());
                    json.put("address", cust.getAddress());
                    json.put("phone", cust.getPhone());
                    json.put("units", cust.getUnitsConsumed());
                    jsonArray.put(json);
                }
                out.print(jsonArray);
            } else {
                // Fetch single customer by account number
                String accNumParam = req.getParameter("accountNumber");
                if (accNumParam == null || accNumParam.trim().isEmpty()) {
                    out.print(new JSONObject().put("error", "Account number is required"));
                    return;
                }
                int accNum = Integer.parseInt(accNumParam);
                JSONObject json = new JSONObject();
                Customer cust = service.get(accNum);
                if (cust != null) {
                    json.put("name", cust.getName());
                    json.put("address", cust.getAddress());
                    json.put("phone", cust.getPhone());
                    json.put("units", cust.getUnitsConsumed());
                } else {
                    json.put("error", "Customer not found");
                }
                out.print(json);
            }
        } catch (NumberFormatException e) {
            out.print(new JSONObject().put("error", "Invalid account number format"));
        } catch (SQLException e) {
            e.printStackTrace();
            out.print(new JSONObject().put("error", "Failed to fetch customer data"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accNumParam = req.getParameter("accountNumber");
        if (accNumParam == null || accNumParam.trim().isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(new JSONObject().put("ok", false).put("message", "Account number is required"));
            return;
        }
        int accNum = Integer.parseInt(accNumParam);
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            boolean deleted = service.delete(accNum);
            json.put("ok", deleted);
            json.put("message", deleted ? "Customer deleted successfully" : "Deletion failed");
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("ok", false);
            json.put("message", "Error deleting customer");
        }
        out.print(json);
    }
}