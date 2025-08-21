package controller;

import models.Customer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.CustomerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DisplayAccountControllerTest {

    private DisplayAccountController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private CustomerService mockService;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockService = mock(CustomerService.class);

        controller = new DisplayAccountController() {
            {
                this.service = mockService;
            }
        };
    }

    @Test
    void testDoGet_AllCustomers() throws Exception {
        when(request.getParameter("action")).thenReturn("all");

        Customer c1 = new Customer();
        c1.setAccountNumber(1); c1.setName("John"); c1.setAddress("Addr1"); c1.setPhone("123"); c1.setUnitsConsumed(5);
        Customer c2 = new Customer();
        c2.setAccountNumber(2); c2.setName("Jane"); c2.setAddress("Addr2"); c2.setPhone("456"); c2.setUnitsConsumed(10);

        when(mockService.getAll()).thenReturn(Arrays.asList(c1, c2));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        JSONArray result = new JSONArray(stringWriter.toString());
        assertEquals(2, result.length());
        assertEquals("John", result.getJSONObject(0).getString("name"));
        assertEquals("Jane", result.getJSONObject(1).getString("name"));
    }

    @Test
    void testDoGet_SingleCustomerFound() throws Exception {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("accountNumber")).thenReturn("1");

        Customer c = new Customer();
        c.setName("John"); c.setAddress("Addr1"); c.setPhone("123"); c.setUnitsConsumed(5);
        when(mockService.get(1)).thenReturn(c);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertEquals("John", result.getString("name"));
        assertEquals(5, result.getInt("units"));
    }

    @Test
    void testDoGet_SingleCustomerNotFound() throws Exception {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("accountNumber")).thenReturn("99");
        when(mockService.get(99)).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertEquals("Customer not found", result.getString("error"));
    }

    @Test
    void testDoGet_InvalidAccountNumber() throws Exception {
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("accountNumber")).thenReturn("abc");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertEquals("Invalid account number format", result.getString("error"));
    }

    @Test
    void testDoDelete_Success() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("1");
        when(mockService.delete(1)).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doDelete(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertTrue(result.getBoolean("ok"));
        assertEquals("Customer deleted successfully", result.getString("message"));
    }

    @Test
    void testDoDelete_Failure() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("1");
        when(mockService.delete(1)).thenReturn(false);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doDelete(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
        assertEquals("Deletion failed", result.getString("message"));
    }

    @Test
    void testDoDelete_SQLException() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("1");
        when(mockService.delete(1)).thenThrow(new SQLException("DB error"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doDelete(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
        assertEquals("Error deleting customer", result.getString("message"));
    }

    @Test
    void testDoDelete_MissingAccountNumber() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doDelete(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
        assertEquals("Account number is required", result.getString("message"));
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
