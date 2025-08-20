package controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import models.User;
import org.json.JSONObject;
import services.UserService;

public class LoginController extends HttpServlet {
    public UserService service = new UserService();
    public static Map<String, Map<String, String>> tokenStore = new HashMap<>(); // {token: {username, role}}

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) pathInfo = "";

        System.out.println("Received POST request for path: " + req.getServletPath() + pathInfo + ", Method: " + req.getMethod()); // Debug output

        if ("/login".equals(pathInfo) || "".equals(pathInfo)) { // Handle root /login path
            String uName = req.getParameter("username");
            String uPass = req.getParameter("password");

            if (uName != null && uPass != null && !uName.isEmpty() && !uPass.isEmpty()) {
                try {
                    User user = getUser(uName, uPass);
                    if (user != null) {
                        String token = java.util.UUID.randomUUID().toString();
                        Map<String, String> userData = new HashMap<>();
                        userData.put("username", uName);
                        userData.put("role", user.getRole());
                        tokenStore.put(token, userData);
                        json.put("ok", true);
                        json.put("token", token);
                        json.put("role", user.getRole());
                    } else {
                        json.put("ok", false);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    json.put("ok", false);
                }
            } else {
                json.put("ok", false);
            }
        } else if ("/verify".equals(pathInfo)) {
            String token = req.getParameter("token");
            Map<String, String> userData = tokenStore.get(token);
            boolean valid = userData != null;
            json.put("valid", valid);
            if (valid) {
                json.put("role", userData.get("role"));
            }
            System.out.println("Verifying token (POST): " + token + ", Valid: " + valid); // Debug output
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }

        out.print(json);
        out.flush();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) pathInfo = "";

        System.out.println("Received GET request for path: " + req.getServletPath() + pathInfo + ", Method: " + req.getMethod()); // Debug output

        if ("/verify".equals(pathInfo)) {
            String token = req.getParameter("token");
            Map<String, String> userData = tokenStore.get(token);
            boolean valid = userData != null;
            json.put("valid", valid);
            if (valid) {
                json.put("role", userData.get("role"));
            }
            System.out.println("Verifying token (GET): " + token + ", Valid: " + valid); // Debug output
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed for this path");
        }

        out.print(json);
        out.flush();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setHeader("Allow", "GET, POST, OPTIONS");
    }

    private User getUser(String uName, String uPass) throws SQLException {
        User user = new User();
        user.setUserName(uName);
        user.setUserPass(uPass);
        if (service.logIn(uName, uPass)) {
            return service.getUser(uName, uPass); // Assume getUser method in UserService
        }
        return null;
    }
}