package controller;

import models.Item;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.ItemService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManageItemControllerTest {

    private ManageItemController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ItemService mockService;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockService = mock(ItemService.class);

        controller = new ManageItemController() {{
            this.service = mockService;
        }};
    }

    @Test
    void testAddItem_Success() throws Exception {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("name")).thenReturn("Book A");
        when(request.getParameter("author")).thenReturn("Author A");
        when(request.getParameter("price")).thenReturn("25.5");
        when(request.getParameter("category")).thenReturn("Fiction");
        when(request.getPart("image")).thenReturn(null); // No file uploaded
        when(mockService.add(any(Item.class))).thenReturn(true);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doPost(request, response);
        pw.flush();

        JSONObject result = new JSONObject(sw.toString());
        assertTrue(result.getBoolean("ok"));
    }

    @Test
    void testUpdateItem_Success() throws Exception {
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("name")).thenReturn("Book A Updated");
        when(request.getParameter("author")).thenReturn("Author B");
        when(request.getParameter("price")).thenReturn("30");
        when(request.getParameter("category")).thenReturn("Non-Fiction");
        when(request.getParameter("id")).thenReturn("1");
        when(request.getPart("image")).thenReturn(null);
        when(mockService.update(any(Item.class))).thenReturn(true);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doPost(request, response);
        pw.flush();

        JSONObject result = new JSONObject(sw.toString());
        assertTrue(result.getBoolean("ok"));
    }

    @Test
    void testDeleteItem_Success() throws Exception {
        when(request.getParameter("action")).thenReturn("delete");
        when(request.getParameter("id")).thenReturn("5");
        when(mockService.delete(5)).thenReturn(true);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doPost(request, response);
        pw.flush();

        JSONObject result = new JSONObject(sw.toString());
        assertTrue(result.getBoolean("ok"));
    }

    @Test
    void testAddItem_MissingParameter() throws Exception {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("name")).thenReturn(null); // missing required
        when(request.getParameter("author")).thenReturn("Author");
        when(request.getParameter("price")).thenReturn("10");
        when(request.getParameter("category")).thenReturn("Fiction");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        controller.doPost(request, response);
        pw.flush();

        JSONObject result = new JSONObject(sw.toString());
        assertFalse(result.getBoolean("ok"));
    }

}
