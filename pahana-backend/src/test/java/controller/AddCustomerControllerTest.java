package controller;

import models.Customer;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.CustomerService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddCustomerControllerTest {

    private AddCustomerController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private CustomerService mockService;

    @BeforeEach
    void setUp() {
        controller = new AddCustomerController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockService = mock(CustomerService.class);

        // Inject mock service into controller
        controller = new AddCustomerController() {
            {
                this.service = mockService;
            }
        };

        // Setup token store
        LoginController.tokenStore = new HashMap<>();
        HashMap<String, String> adminData = new HashMap<>();
        adminData.put("role", "Admin");
        LoginController.tokenStore.put("valid-admin-token", adminData);
    }

    @Test
    void testDoPost_Success() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-admin-token");
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("address")).thenReturn("123 Street");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(mockService.add(any(Customer.class))).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);

        writer.flush();
        JSONObject result = new JSONObject(stringWriter.toString());
        assertTrue(result.getBoolean("ok"));
        assertFalse(result.has("message"));
    }

    @Test
    void testDoPost_Unauthorized() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");

        controller.doPost(request, response);

        verify(response).sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Admin role required");
    }

    @Test
    void testDoPost_Failure() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-admin-token");
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("address")).thenReturn("123 Street");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(mockService.add(any(Customer.class))).thenReturn(false);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);

        writer.flush();
        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
        assertEquals("Failed to add customer (maybe duplicate or DB error).", result.getString("message"));
    }

    @Test
    void testDoPost_Exception() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-admin-token");
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("address")).thenReturn("123 Street");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(mockService.add(any(Customer.class))).thenThrow(new SQLException("DB error"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);

        writer.flush();
        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
        assertEquals("DB error", result.getString("message"));
    }
}
