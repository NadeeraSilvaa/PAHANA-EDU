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

public class EditCustomerController extends HttpServlet {
    CustomerService service = new CustomerService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int accNum = Integer.parseInt(req.getParameter("accountNumber"));
        String name = req.getParameter("name");
        String addr = req.getParameter("address");
        String phone = req.getParameter("phone");
        int units = Integer.parseInt(req.getParameter("units"));

        Customer cust = new Customer();
        cust.setAccountNumber(accNum);
        cust.setName(name);
        cust.setAddress(addr);
        cust.setPhone(phone);
        cust.setUnitsConsumed(units);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            boolean ok = service.edit(cust);
            json.put("ok", ok);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.print(json);
    }
}