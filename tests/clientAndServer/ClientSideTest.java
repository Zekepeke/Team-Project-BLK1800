package tests.clientAndServer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.client.ClientSide;
import src.server.Server;

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
        // Start the server
        server = new Server(8282);
        server.startup();
        // Initialize the client
        Socket socket = new Socket("localhost", 8282);
        client = new ClientSide(socket);
        client.sendHandShake();
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
