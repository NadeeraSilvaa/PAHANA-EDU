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

public class GetAllItemsController extends HttpServlet {
    ItemService service = new ItemService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        try {
            List<Item> books = service.getAll();
            JSONArray jsonArray = new JSONArray();
            for (Item book : books) {
                JSONObject json = new JSONObject();
                json.put("id", book.getItemId());
                json.put("name", book.getItemName());
                json.put("author", book.getAuthor());
                json.put("price", book.getItemPrice());
                json.put("imageUrl", book.getImageUrl() != null ? book.getImageUrl() : "");
                json.put("category", book.getCategory() != null ? book.getCategory() : "");
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