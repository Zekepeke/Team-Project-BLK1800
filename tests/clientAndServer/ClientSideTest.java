package tests.clientAndServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.client.ClientSide;
import src.Server.Server;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ClientSideTest {

    private ClientSide client;
    private Server server;
    private final String user = "bob";
    private final String password = "password456";

    @BeforeEach
    void setUp() throws IOException {
        // Start the server in a separate thread
        server = new Server(8282);
        new Thread(() -> {
            server.startup();
        }).start();

        // Allow time for the server to start up
        try {
            Thread.sleep(500); // Adjust if needed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Initialize the client
        Socket socket = new Socket("localhost", 8282);
        client = new ClientSide(socket);
        client.sendHandShake();
    }
    @AfterEach
    void tearDown() throws IOException {
        if (server != null) {
            server.shutdown(); // Gracefully shut down the server
        }
    }

    @Test
    void testValidUser() {
        assertTrue(client.validUserAndPassword(user, password), "User validation failed.");
    }

    @Test
    void testConnectionToServer() {
        assertDoesNotThrow(() -> client.connectToServer(), "Connection to server should not throw exceptions.");
    }


    @Test
    void testProfileSearch() {
        String[] profile = client.profile();
        assertNotNull(profile, "Profile should not be null.");
        for (String s : profile) {
            System.out.println(s);
        }
    }
}
