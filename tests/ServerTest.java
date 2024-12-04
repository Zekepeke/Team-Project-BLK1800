package tests;

import org.junit.*;
import src.Server.Server;
import src.Server.ClientCommunicationHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Test class for the Server class.
 */
public class ServerTest {

    private static final int TEST_PORT = 8484; // Use a specific port for tests
    private Server testServer;
    private Thread serverThread;

    @Before
    public void setUp() {
        // Initialize the Server instance
        testServer = new Server(TEST_PORT);

        // Start the Server in a separate thread
        serverThread = new Thread(() -> testServer.startup());
        serverThread.start();

    }

    @After
    public void tearDown() {
        // Stop the server and ensure all threads are interrupted
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
        }
        for (Thread thread : Server.activeConversations) {
            thread.interrupt();
        }
    }

    @org.junit.Test
    public void testServerInitialization() {
        assertNotNull("Server should initialize correctly.", testServer);
        assertEquals("No active conversations should exist initially.", 0, Server.activeConversations.size());
        assertEquals("No sockets should exist initially.", 0, Server.sockets.size());
    }

    @org.junit.Test
    public void testServerAcceptsClientConnection() {
        try (Socket clientSocket = new Socket("localhost", TEST_PORT)) {
            assertTrue("Client socket should be connected.", clientSocket.isConnected());
            Thread.sleep(100); // Allow time for the server to process the connection
            assertEquals("Server should have one client socket.", 1, Server.sockets.size());
            assertEquals("Server should have one active conversation.", 1, Server.activeConversations.size());
        } catch (IOException | InterruptedException e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testCleanUpInactiveConversations() throws InterruptedException {
        // Create and add mock ClientCommunicationHandler threads
        ClientCommunicationHandler activeHandler = new ClientCommunicationHandler(new Socket());
        ClientCommunicationHandler inactiveHandler = new ClientCommunicationHandler(new Socket());

        activeHandler.start();
        inactiveHandler.start();
        inactiveHandler.interrupt(); // Simulate an inactive thread

        Server.activeConversations.add(activeHandler);
        Server.activeConversations.add(inactiveHandler);

        // Wait for threads to reflect their states
        Thread.sleep(200);

        // Call the cleanup method via reflection
        invokeCleanUp();

        assertEquals("Only active threads should remain.", 1, Server.activeConversations.size());
        assertTrue("Active thread should still be in the list.", Server.activeConversations.contains(activeHandler));
        assertFalse("Inactive thread should be removed.", Server.activeConversations.contains(inactiveHandler));
    }

    @org.junit.Test
    public void testServerHandlesMultipleClients() {
        try (Socket client1 = new Socket("localhost", TEST_PORT);
             Socket client2 = new Socket("localhost", TEST_PORT)) {

            assertTrue("First client should be connected.", client1.isConnected());
            assertTrue("Second client should be connected.", client2.isConnected());
            Thread.sleep(100); // Allow time for the server to process connections

            assertEquals("Server should have two client sockets.", 2, Server.sockets.size());
            assertEquals("Server should have two active conversations.", 2, Server.activeConversations.size());
        } catch (IOException | InterruptedException e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    // Helper method to invoke private cleanUpInactiveConversations
    private void invokeCleanUp() {
        try {
            Server.class.getDeclaredMethod("cleanUpInactiveConversations").setAccessible(true);
            Server.class.getDeclaredMethod("cleanUpInactiveConversations").invoke(testServer);
        } catch (Exception e) {
            fail("Failed to invoke cleanUpInactiveConversations: " + e.getMessage());
        }
    }
}
