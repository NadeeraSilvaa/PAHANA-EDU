package controller;

import models.Bill;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.BillService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViewBillsControllerTest {

    private ViewBillsController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private BillService mockService;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockService = mock(BillService.class);

        controller = new ViewBillsController() {{
            this.service = mockService;
        }};
    }

    @Test
    void testViewBills_Success() throws Exception {
        when(request.getParameter("customerId")).thenReturn("1");

        Bill bill1 = new Bill();
        bill1.setId(101);
        bill1.setBillAmount(50.5);
        bill1.setCreatedAt(Timestamp.valueOf("2025-08-20 10:30:00")); // Correct format

        Bill bill2 = new Bill();
        bill2.setId(102);
        bill2.setBillAmount(75.0);
        bill2.setCreatedAt(Timestamp.valueOf("2025-08-21 15:45:00")); // Correct format

        List<Bill> bills = Arrays.asList(bill1, bill2);
        when(mockService.getBillsByCustomer(1)).thenReturn(bills);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doGet(request, response);
        pw.flush();

        JSONObject result = new JSONObject(sw.toString());
        JSONArray billArray = result.getJSONArray("bills");

        assertEquals(2, billArray.length());
        assertEquals(101, billArray.getJSONObject(0).getInt("id"));
        assertEquals(50.5, billArray.getJSONObject(0).getDouble("billAmount"));
        assertEquals("2025-08-20 10:30:00.0", billArray.getJSONObject(0).getString("createdAt"));

        assertEquals(102, billArray.getJSONObject(1).getInt("id"));
        assertEquals(75.0, billArray.getJSONObject(1).getDouble("billAmount"));
        assertEquals("2025-08-21 15:45:00.0", billArray.getJSONObject(1).getString("createdAt"));
    }

    @Test
    void testViewBills_ServiceThrowsSQLException() throws Exception {
        when(request.getParameter("customerId")).thenReturn("1");
        when(mockService.getBillsByCustomer(1)).thenThrow(new SQLException("DB error"));

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doGet(request, response);
        pw.flush();

        JSONObject result = new JSONObject(sw.toString());
        assertTrue(result.has("error"));
        assertEquals("Failed to fetch bills", result.getString("error"));
    }

    @Test
    void testViewBills_InvalidCustomerId() {
        assertThrows(NumberFormatException.class, () -> {
            when(request.getParameter("customerId")).thenReturn("invalid");
            controller.doGet(request, response);
        });
    }
}
