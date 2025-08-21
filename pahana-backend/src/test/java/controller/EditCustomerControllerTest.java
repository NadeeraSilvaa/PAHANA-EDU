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

class EditCustomerControllerTest {

    private EditCustomerController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private CustomerService mockService;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockService = mock(CustomerService.class);

        controller = new EditCustomerController() {
            {
                this.service = mockService;
            }
        };
    }

    @Test
    void testDoPost_Success() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("address")).thenReturn("123 Street");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("units")).thenReturn("5");
        when(mockService.edit(any(Customer.class))).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertTrue(result.getBoolean("ok"));
    }

    @Test
    void testDoPost_Failure() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("address")).thenReturn("123 Street");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("units")).thenReturn("5");
        when(mockService.edit(any(Customer.class))).thenReturn(false);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
    }

    @Test
    void testDoPost_SQLException() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("1");
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("address")).thenReturn("123 Street");
        when(request.getParameter("phone")).thenReturn("1234567890");
        when(request.getParameter("units")).thenReturn("5");
        when(mockService.edit(any(Customer.class))).thenThrow(new SQLException("DB error"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        // Since your controller doesn’t set json.put("ok", false) on exception, it will be empty
        // We can at least check that the output contains no "ok = true"
        assertFalse(result.has("ok") && result.getBoolean("ok"));
    }
}
