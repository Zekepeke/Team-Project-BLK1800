package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import src.client.ClientSide;
import java.io.IOException;
import java.net.Socket;

public class ClientSideTest {
    private ClientSide client1;
    private ClientSide client2;
    private ClientSide client3;
    private Socket userClient;
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    @Before
    public void setUp() throws IOException {
        // Create a client socket
        userClient = new Socket(HOST, PORT);

        // Testing
        client1 = new ClientSide(userClient);

        client2 = new ClientSide(userClient);

        client3 = new ClientSide(userClient);
    }

    @Test
    public void validUserAndPassword() {
        String user1Username = "somethinguser";
        String user1Password = "KSILOVER";

        String user2Username = "Bob";
        String user2Password = "password456";

        String user3Username = "Johnis∑&Cool";
        String user3Password = "password456";

        assertTrue(client1.validUserAndPassword(user1Username, user1Password));
        client1.setUsername(user1Username);
        client1.setPassword(user1Password);
        assertFalse(client1.validUserAndPassword(user2Username, user2Password));
        assertFalse(client1.validUserAndPassword(user3Username, user3Password));
    }

    @Test
    public void testClientConstructor() {
        assertNotNull("Client instance should be created", client1);
    }

    @Test
    public void testConnectToServer() {
        boolean result = client1.connectToServer();
        assertTrue("Client should connect to server successfully", result);
    }

}
