package controller;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.HelpUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HelpControllerTest {

    private HelpController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        controller = new HelpController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void testDoGet_ReturnsHelpText() throws Exception {
        // Capture output
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Call the controller
        controller.doGet(request, response);
        writer.flush();

        // Convert output to JSON
        JSONObject result = new JSONObject(stringWriter.toString());

        // Assertions
        assertTrue(result.has("help"));
        assertEquals(HelpUtil.getText(), result.getString("help"));
    }

    @Test
    void testDoGet_ContentTypeIsJson() throws Exception {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        controller.doGet(request, response);
        writer.flush();

        // Verify content type
        verify(response).setContentType("application/json");
    }
}
