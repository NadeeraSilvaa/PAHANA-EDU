package controller;

import models.Item;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ItemService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchItemsControllerTest {

    private SearchItemsController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ItemService mockService;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockService = mock(ItemService.class);

        controller = new SearchItemsController() {{
            this.service = mockService;
        }};
    }

    @Test
    void testSearchItems_Success() throws Exception {
        when(request.getParameter("name")).thenReturn("Book");

        Item item1 = new Item();
        item1.setItemId(1);
        item1.setItemName("Book A");
        item1.setAuthor("Author A");
        item1.setItemPrice(10.5);

        Item item2 = new Item();
        item2.setItemId(2);
        item2.setItemName("Book B");
        item2.setAuthor("Author B");
        item2.setItemPrice(15.0);

        List<Item> items = Arrays.asList(item1, item2);
        when(mockService.searchByName("Book")).thenReturn(items);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doGet(request, response);
        pw.flush();

        JSONArray result = new JSONArray(sw.toString());
        assertEquals(2, result.length());
        assertEquals("Book A", result.getJSONObject(0).getString("name"));
        assertEquals("Book B", result.getJSONObject(1).getString("name"));
    }

    @Test
    void testSearchItems_NoNameParameter() throws Exception {
        when(request.getParameter("name")).thenReturn(null);

        controller.doGet(request, response);
        verify(response).setStatus(400);
    }

    @Test
    void testSearchItems_ServiceThrowsSQLException() throws Exception {
        when(request.getParameter("name")).thenReturn("Book");
        when(mockService.searchByName("Book")).thenThrow(new SQLException("DB error"));

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doGet(request, response);
        pw.flush();

        verify(response).setStatus(500);
        assertEquals("[]", sw.toString());
    }
}
