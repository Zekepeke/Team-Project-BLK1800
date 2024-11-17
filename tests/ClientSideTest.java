import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import src.client.ClientSide;

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
        boolean result = client.connectToServer("localhost", 5000);
        assertTrue("Client should connect to server successfully", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectToServerWithInvalidAddress() {
        client.connectToServer(null, 5000);
    }

    @Test
    public void testDisconnectFromServer() {
        boolean result = client.disconnectFromServer();
        assertTrue("Client should disconnect from server successfully", result);
    }
}
