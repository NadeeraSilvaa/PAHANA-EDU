package controller;

import models.Customer;
import models.Item;
import models.Bill;
import org.json.JSONArray;
import org.json.JSONObject;
import services.ItemService;
import services.BillService;
import services.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class CalculateBillController extends HttpServlet {
    ItemService itemService = new ItemService();
    BillService billService = new BillService();
    CustomerService customerService = new CustomerService();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            // Read JSON payload
            String requestBody = req.getReader().lines().collect(Collectors.joining());
            JSONObject requestJson = new JSONObject(requestBody);
            String customerIdStr = requestJson.getString("customerId");
            JSONArray booksArray = requestJson.getJSONArray("books");

            if (customerIdStr == null || customerIdStr.isEmpty() || booksArray.isEmpty()) {
                json.put("ok", false);
                json.put("error", "Invalid input");
                out.print(json);
                return;
            }

            int customerId = Integer.parseInt(customerIdStr);

            // Verify customer exists
            Customer customer = customerService.get(customerId);
            if (customer == null) {
                json.put("ok", false);
                json.put("error", "Customer with ID " + customerId + " not found");
                out.print(json);
                return;
            }

            double total = 0.0;
            int totalUnits = 0;
            for (int i = 0; i < booksArray.length(); i++) {
                JSONObject bookJson = booksArray.getJSONObject(i);
                int bookId = bookJson.getInt("bookId");
                int quantity = bookJson.getInt("quantity");
                Item item = itemService.getById(bookId);
                if (item == null) {
                    json.put("ok", false);
                    json.put("error", "Book with ID " + bookId + " not found");
                    out.print(json);
                    return;
                }
                total += item.getItemPrice() * quantity;
                totalUnits += quantity;
            }

            // Save the bill
            Bill bill = new Bill();
            bill.setCustomerId(customerId);
            bill.setBillAmount(total);
            boolean saved = billService.save(bill);

            if (!saved) {
                json.put("ok", false);
                json.put("error", "Failed to save bill");
                out.print(json);
                return;
            }

            // Update customer's unitsConsumed
            boolean unitsUpdated = customerService.updateUnits(customerId, totalUnits);
            if (!unitsUpdated) {
                json.put("ok", false);
                json.put("error", "Failed to update customer units");
                out.print(json);
                return;
            }

            json.put("ok", true);
            json.put("total", total);
        } catch (SQLException e) {
            e.printStackTrace();
            json.put("ok", false);
            json.put("error", "Database error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            json.put("ok", false);
            json.put("error", "Invalid request");
        }
        out.print(json);
    }
}