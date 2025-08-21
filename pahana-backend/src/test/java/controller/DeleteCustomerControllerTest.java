package controller;

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

class DeleteCustomerControllerTest {

    private DeleteCustomerController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private CustomerService mockService;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockService = mock(CustomerService.class);

        // Inject mock service
        controller = new DeleteCustomerController() {
            {
                this.service = mockService;
            }
        };
    }

    @Test
    void testDoPost_Success() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("123");
        when(mockService.delete(123)).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertTrue(result.getBoolean("ok"));
        assertFalse(result.has("error"));
    }

    @Test
    void testDoPost_Failure() throws Exception {
        when(request.getParameter("accountNumber")).thenReturn("123");
        when(mockService.delete(123)).thenReturn(false);

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
        when(request.getParameter("accountNumber")).thenReturn("123");
        when(mockService.delete(123)).thenThrow(new SQLException("DB error"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertEquals("Failed to delete customer", result.getString("error"));
    }
}
