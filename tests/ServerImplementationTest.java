package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import src.ServerImplementation;

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
}
