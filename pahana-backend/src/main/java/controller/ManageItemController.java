package controller;

import models.Item;
import org.json.JSONObject;
import services.ItemService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ManageItemController extends HttpServlet {
    private ItemService service = new ItemService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String act = req.getParameter("action");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            if ("add".equals(act)) {
                String name = req.getParameter("name");
                double price = Double.parseDouble(req.getParameter("price"));
                Item it = new Item();
                it.setItemName(name);
                it.setItemPrice(price);
                json.put("ok", service.add(it));
            } else if ("update".equals(act)) {
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                double price = Double.parseDouble(req.getParameter("price"));
                Item it = new Item();
                it.setItemId(id);
                it.setItemName(name);
                it.setItemPrice(price);
                json.put("ok", service.update(it));
            } else if ("delete".equals(act)) {
                int id = Integer.parseInt(req.getParameter("id"));
                json.put("ok", service.delete(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.print(json);
    }
}