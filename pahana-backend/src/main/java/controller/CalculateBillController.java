package controller;

import models.Bill;
import org.json.JSONObject;
import services.BillService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class CalculateBillController extends HttpServlet {
    private BillService service = new BillService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int custId = Integer.parseInt(req.getParameter("customerId"));
        int units = Integer.parseInt(req.getParameter("units"));
        double total = service.calc(units);

        Bill b = new Bill();
        b.setCustomerId(custId);
        b.setBillAmount(total);

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            boolean ok = service.save(b);
            json.put("total", total);
            json.put("ok", ok);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.print(json);
    }
}