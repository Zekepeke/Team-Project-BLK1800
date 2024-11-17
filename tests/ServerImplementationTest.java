package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import src.ServerImplementation;
import java.io.IOException;
import java.net.Socket;

public class ServerImplementationTest {
    private ServerImplementation server;

    @Before
    public void setUp() {
        server = new ServerImplementation(8080);
    }

    @Test
    public void testServerConstructor() {
        assertNotNull("Server instance should be created", server);
    }

    @Test
    public void testStartup() {
        boolean result = server.startup();
        assertTrue("Server should start up successfully", result);
    }

    @Test
    public void testCurrentUsers() {
        assertNotNull("Current users list should be initialized", server.currentUsers);
    }

    @Test
    public void testUserNetMap() {
        assertNotNull("User network map should be initialized", server.userNetMap);
    }

    @Test
    public void testServerHandlesClientRequests() {
        server.startup();
        try (Socket clientSocket = new Socket("localhost", 8080)) {
            clientSocket.getOutputStream().write("LOGIN user1".getBytes());
            clientSocket.getOutputStream().flush();

            byte[] response = new byte[1024];
            int bytesRead = clientSocket.getInputStream().read(response);
            String serverResponse = new String(response, 0, bytesRead);

            assertEquals("Expected server response", "LOGIN_SUCCESS", serverResponse.trim());
        } catch (IOException e) {
            fail("Error testing client requests: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testServerErrorHandling() {
        new ServerImplementation(-1);
    }
}
