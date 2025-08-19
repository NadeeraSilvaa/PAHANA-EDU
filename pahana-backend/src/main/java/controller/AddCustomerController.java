package controller;

import models.Customer;
import org.json.JSONObject;
import services.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class AddCustomerController extends HttpServlet {
    private CustomerService service = new CustomerService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String addr = req.getParameter("address");
        String phone = req.getParameter("phone");
        int units = Integer.parseInt(req.getParameter("units"));

        Customer cust = new Customer();
        cust.setName(name);
        cust.setAddress(addr);
        cust.setPhone(phone);
        cust.setUnitsConsumed(units);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            boolean ok = service.add(cust);
            json.put("ok", ok);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.print(json);
    }
}