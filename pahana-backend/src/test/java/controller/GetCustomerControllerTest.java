package controller;

import models.Customer;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.CustomerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetCustomerControllerTest {

    private GetCustomerController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private CustomerService mockService;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockService = mock(CustomerService.class);

        controller = new GetCustomerController() {
            {
                this.service = mockService;
            }
        };
    }

    @Test
    void testDoGet_CustomerFound() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("1");

        Customer c = new Customer();
        c.setAccountNumber(1); c.setName("John"); c.setAddress("Addr"); c.setPhone("123456");
        when(mockService.get(1)).thenReturn(c);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertTrue(result.getBoolean("ok"));
        assertEquals("John", result.getJSONObject("customer").getString("name"));
    }

    @Test
    void testDoGet_CustomerNotFound() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("2");
        when(mockService.get(2)).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
        assertEquals("Customer not found", result.getString("message"));
    }

    @Test
    void testDoGet_InvalidAccountNumber() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("abc");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
        assertEquals("Invalid account number", result.getString("message"));
    }

    @Test
    void testDoGet_SQLException() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("1");
        when(mockService.get(1)).thenThrow(new SQLException("DB error"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
        assertTrue(result.getString("message").contains("Database error"));
    }
}
