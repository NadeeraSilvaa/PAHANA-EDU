package controller;

import models.Item;
import org.json.JSONArray;
import org.json.JSONObject;
import services.ItemService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class SearchItemsController extends HttpServlet {
    private ItemService service = new ItemService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        if (name == null || name.isEmpty()) {
            resp.setStatus(400);
            return;
        }
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        try {
            List<Item> items = service.searchByName(name);
            JSONArray jsonArray = new JSONArray();
            for (Item item : items) {
                JSONObject json = new JSONObject();
                json.put("id", item.getItemId());
                json.put("name", item.getItemName());
                json.put("author", item.getAuthor()); // Include author for consistency
                json.put("price", item.getItemPrice());
                jsonArray.put(json);
            }
            out.print(jsonArray);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(500);
            out.print("[]");
        }
    }
}