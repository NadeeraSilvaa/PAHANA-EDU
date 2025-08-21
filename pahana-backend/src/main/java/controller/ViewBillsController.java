package controller;

import models.Bill;
import org.json.JSONArray;
import org.json.JSONObject;
import services.BillService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class ViewBillsController extends HttpServlet {
    BillService service = new BillService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int custId = Integer.parseInt(req.getParameter("customerId"));
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            List<Bill> bills = service.getBillsByCustomer(custId);
            JSONArray billArray = new JSONArray();
            for (Bill bill : bills) {
                JSONObject billObj = new JSONObject();
                billObj.put("id", bill.getId());
                billObj.put("billAmount", bill.getBillAmount());
                billObj.put("createdAt", bill.getCreatedAt());
                billArray.put(billObj);
            }
            json.put("bills", billArray);
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("error", "Failed to fetch bills");
        }
        out.print(json);
    }
}