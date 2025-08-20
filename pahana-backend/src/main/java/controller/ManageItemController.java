package controller;

import jakarta.servlet.annotation.MultipartConfig;
import models.Item;
import org.json.JSONObject;
import services.ItemService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 5,      // 5MB
        maxRequestSize = 1024 * 1024 * 50)  // 50MB
public class ManageItemController extends HttpServlet {
    private ItemService service = new ItemService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String act = req.getParameter("action");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            if ("add".equals(act) || "update".equals(act)) {
                String name = req.getParameter("name");
                String author = req.getParameter("author");
                String priceStr = req.getParameter("price");
                String category = req.getParameter("category");
                Part imagePart = req.getPart("image");

                if (name == null || author == null || priceStr == null || category == null) {
                    json.put("ok", false);
                    out.print(json);
                    return;
                }

                double price = Double.parseDouble(priceStr);
                String imageUrl = null;
                if (imagePart != null && imagePart.getSize() > 0) {
                    String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
                    String uploadPath = getServletContext().getRealPath("") + "uploads";
                    Files.createDirectories(Paths.get(uploadPath));
                    String filePath = uploadPath + "/" + fileName;
                    Files.copy(imagePart.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
                    imageUrl = "uploads/" + fileName;
                }

                Item it = new Item();
                it.setItemName(name);
                it.setAuthor(author);
                it.setItemPrice(price);
                it.setCategory(category);
                it.setImageUrl(imageUrl);

                if ("add".equals(act)) {
                    json.put("ok", service.add(it));
                } else {
                    int id = Integer.parseInt(req.getParameter("id"));
                    it.setItemId(id);
                    json.put("ok", service.update(it));
                }
            } else if ("delete".equals(act)) {
                int id = Integer.parseInt(req.getParameter("id"));
                json.put("ok", service.delete(id));
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            json.put("ok", false);
        }
        out.print(json);
    }
}