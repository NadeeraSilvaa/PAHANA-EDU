package controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.UUID;
import org.json.JSONObject;
import services.UserService;

public class LoginController extends HttpServlet {
    private UserService service = new UserService();
    private static final java.util.Map<String, String> tokenStore = new java.util.HashMap<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject json = new JSONObject();

        String path = req.getServletPath();
        if ("/login".equals(path)) {
            String uName = req.getParameter("username");
            String uPass = req.getParameter("password");

            if (uName != null && uPass != null && !uName.isEmpty() && !uPass.isEmpty()) {
                try {
                    boolean ok = service.logIn(uName, uPass);
                    if (ok) {
                        String token = UUID.randomUUID().toString();
                        tokenStore.put(token, uName);
                        json.put("ok", true);
                        json.put("token", token);
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
        } else if ("/verify".equals(path)) {
            String token = req.getParameter("token");
            boolean valid = token != null && tokenStore.containsKey(token);
            json.put("valid", valid);
        }

        out.print(json);
        out.flush();
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}