package controller;

import models.Item;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ItemService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetAllItemsControllerTest {

    private GetAllItemsController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ItemService mockService;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockService = mock(ItemService.class);

        controller = new GetAllItemsController() {
            {
                this.service = mockService;
            }
        };
    }

    @Test
    void testDoGet_Success() throws Exception {
        Item item1 = new Item();
        item1.setItemId(101); item1.setItemName("Book1"); item1.setAuthor("Author1"); item1.setItemPrice(10.0);
        Item item2 = new Item();
        item2.setItemId(102); item2.setItemName("Book2"); item2.setAuthor("Author2"); item2.setItemPrice(20.0);

        when(mockService.getAll()).thenReturn(Arrays.asList(item1, item2));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        JSONArray result = new JSONArray(stringWriter.toString());
        assertEquals(2, result.length());
        assertEquals("Book1", result.getJSONObject(0).getString("name"));
        assertEquals(20.0, result.getJSONObject(1).getDouble("price"));
    }

    @Test
    void testDoGet_EmptyList() throws Exception {
        when(mockService.getAll()).thenReturn(Collections.emptyList());

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        JSONArray result = new JSONArray(stringWriter.toString());
        assertEquals(0, result.length());
    }

    @Test
    void testDoGet_SQLException() throws Exception {
        when(mockService.getAll()).thenThrow(new SQLException("DB error"));

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        // Response should be empty JSON array
        JSONArray result = new JSONArray(stringWriter.toString());
        assertEquals(0, result.length());

        // Response status should be set to 500
        verify(response).setStatus(500);
    }
}
