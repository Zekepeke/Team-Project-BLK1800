package tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import src.client.ClientSide;
import java.io.IOException;

public class ClientSideTest {
    private ClientSide client;

    @Before
    public void setUp() {
        client = new ClientSide();
    }

    @Test
    public void testClientConstructor() {
        assertNotNull("Client instance should be created", client);
    }

    @Test
    public void testConnectToServer() {
        boolean result = client.connectToServer("localhost", 8080);
        assertTrue("Client should connect to server successfully", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectToServerWithInvalidAddress() {
        client.connectToServer(null, 8080);
    }

    @Test
    public void testDisconnectFromServer() {
        boolean result = client.disconnectFromServer();
        assertTrue("Client should disconnect from server successfully", result);
    }

    @Test
    public void testSendAndReceiveComplexMessage() {
        try {
            client.connectToServer("localhost", 8080);
            client.sendMessage("LOGIN user1");
            String response = client.receiveMessage();
            assertEquals("Expected response for login", "LOGIN_SUCCESS", response);

            client.sendMessage("SEND_MESSAGE Hello");
            response = client.receiveMessage();
            assertEquals("Expected response for sending message", "MESSAGE_RECEIVED", response);
        } catch (IOException e) {
            fail("Error in send/receive complex message: " + e.getMessage());
        }
    }
}
