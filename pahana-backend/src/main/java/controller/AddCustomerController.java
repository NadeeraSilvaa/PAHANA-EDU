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
import java.util.Map;

public class AddCustomerController extends HttpServlet {
    protected CustomerService service = new CustomerService();

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tokenHeader = req.getHeader("Authorization");
        String token = (tokenHeader != null && tokenHeader.startsWith("Bearer ")) ? tokenHeader.substring(7) : null;
        if (!isAuthorized(token, "Admin")) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Admin role required");
            return;
        }

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        String name = req.getParameter("name");
        String addr = req.getParameter("address");
        String phone = req.getParameter("phone");

        Customer cust = new Customer();
        cust.setName(name);
        cust.setAddress(addr);
        cust.setPhone(phone);

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

    private boolean isAuthorized(String token, String requiredRole) {
        if (token == null) return false;
        Map<String, String> userData = LoginController.tokenStore.get(token);
        return userData != null && userData.get("role") != null && userData.get("role").equals(requiredRole);
    }
}