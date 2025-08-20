package controller;

import models.Customer;
import models.Item;
import models.Bill;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.BillService;
import services.CustomerService;
import services.ItemService;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculateBillControllerTest {

    private CalculateBillController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private ItemService mockItemService;
    private BillService mockBillService;
    private CustomerService mockCustomerService;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        mockItemService = mock(ItemService.class);
        mockBillService = mock(BillService.class);
        mockCustomerService = mock(CustomerService.class);

        controller = new CalculateBillController() {
            {
                this.itemService = mockItemService;
                this.billService = mockBillService;
                this.customerService = mockCustomerService;
            }
        };
    }

    @Test
    void testDoPost_Success() throws Exception {
        // Mock request JSON
        String requestBody = """
                {
                    "customerId": "1",
                    "books": [
                        {"bookId": 101, "quantity": 2},
                        {"bookId": 102, "quantity": 3}
                    ]
                }
                """;

        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(requestBody)));

        // Mock services
        Customer customer = new Customer();
        when(mockCustomerService.get(1)).thenReturn(customer);

        Item item1 = new Item();
        item1.setItemPrice(10.0);
        Item item2 = new Item();
        item2.setItemPrice(20.0);

        when(mockItemService.getById(101)).thenReturn(item1);
        when(mockItemService.getById(102)).thenReturn(item2);

        when(mockBillService.save(any(Bill.class))).thenReturn(true);
        when(mockCustomerService.updateUnits(1, 5)).thenReturn(true);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Call controller
        controller.doPost(request, response);

        writer.flush();
        JSONObject result = new JSONObject(stringWriter.toString());
        assertTrue(result.getBoolean("ok"));
        assertEquals(10*2 + 20*3, result.getDouble("total"));
    }

    @Test
    void testDoPost_CustomerNotFound() throws Exception {
        String requestBody = """
                {
                    "customerId": "99",
                    "books": [{"bookId": 101, "quantity": 1}]
                }
                """;

        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(requestBody)));
        when(mockCustomerService.get(99)).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
        assertTrue(result.getString("error").contains("Customer with ID 99 not found"));
    }

    @Test
    void testDoPost_ItemNotFound() throws Exception {
        String requestBody = """
                {
                    "customerId": "1",
                    "books": [{"bookId": 999, "quantity": 1}]
                }
                """;

        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(requestBody)));
        when(mockCustomerService.get(1)).thenReturn(new Customer());
        when(mockItemService.getById(999)).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
        assertTrue(result.getString("error").contains("Book with ID 999 not found"));
    }

    @Test
    void testDoPost_InvalidInput() throws Exception {
        String requestBody = "{}"; // Empty JSON
        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(requestBody)));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doPost(request, response);
        writer.flush();

        JSONObject result = new JSONObject(stringWriter.toString());
        assertFalse(result.getBoolean("ok"));
        assertEquals("Invalid input", result.getString("error"));
    }
}
