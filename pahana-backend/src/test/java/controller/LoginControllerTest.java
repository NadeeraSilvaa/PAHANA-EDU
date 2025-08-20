package controller;

import models.User;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private LoginController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserService mockService;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockService = mock(UserService.class);

        controller = new LoginController() {
            {
                this.service = mockService;
            }
        };
        LoginController.tokenStore.clear(); // Reset token store before each test
    }

    @Test
    void testDoPost_LoginSuccess() throws Exception {
        when(request.getPathInfo()).thenReturn("/login");
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("pass");

        User user = new User();
        user.setUserName("admin");
        user.setUserPass("pass");
        user.setRole("Admin");

        when(mockService.logIn("admin", "pass")).thenReturn(true);
        when(mockService.getUser("admin", "pass")).thenReturn(user);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doPost(request, response);
        pw.flush();

        JSONObject result = new JSONObject(sw.toString());
        assertTrue(result.getBoolean("ok"));
        assertEquals("Admin", result.getString("role"));
        assertTrue(result.has("token"));
        assertTrue(LoginController.tokenStore.containsKey(result.getString("token")));
    }

    @Test
    void testDoPost_LoginFailed() throws Exception {
        when(request.getPathInfo()).thenReturn("/login");
        when(request.getParameter("username")).thenReturn("admin");
        when(request.getParameter("password")).thenReturn("wrong");
        when(mockService.logIn("admin", "wrong")).thenReturn(false);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doPost(request, response);
        pw.flush();

        JSONObject result = new JSONObject(sw.toString());
        assertFalse(result.getBoolean("ok"));
    }

    @Test
    void testDoPost_VerifyToken_Post() throws Exception {
        String token = UUID.randomUUID().toString();
        LoginController.tokenStore.put(token, Map.of("username", "admin", "role", "Admin"));

        when(request.getPathInfo()).thenReturn("/verify");
        when(request.getParameter("token")).thenReturn(token);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doPost(request, response);
        pw.flush();

        JSONObject result = new JSONObject(sw.toString());
        assertTrue(result.getBoolean("valid"));
        assertEquals("Admin", result.getString("role"));
    }

    @Test
    void testDoGet_VerifyToken_Get() throws Exception {
        String token = UUID.randomUUID().toString();
        LoginController.tokenStore.put(token, Map.of("username", "admin", "role", "Admin"));

        when(request.getPathInfo()).thenReturn("/verify");
        when(request.getParameter("token")).thenReturn(token);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doGet(request, response);
        pw.flush();

        JSONObject result = new JSONObject(sw.toString());
        assertTrue(result.getBoolean("valid"));
        assertEquals("Admin", result.getString("role"));
    }

    @Test
    void testDoPost_InvalidPath() throws Exception {
        when(request.getPathInfo()).thenReturn("/unknown");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doPost(request, response);
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
    }

    @Test
    void testDoGet_InvalidPath() throws Exception {
        when(request.getPathInfo()).thenReturn("/login");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed for this path");
    }
}
