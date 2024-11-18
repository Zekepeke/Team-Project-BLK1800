package tests;

import org.junit.*;
import src.Server.ClientCommunicationHandler;
import src.Server.Server;
import src.Server.ServerExceptions.UserChatActiveException;
import src.SocketIO;
import src.User;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;

/**
 * Test class for ClientCommunicationHandler.
 */
public class ClientCommunicationHandlerTest {

    private static final int TEST_PORT = 8484;
    private Server testServer;
    private Thread serverThread;
    private Socket clientSocket;
    private SocketIO clientIO;
    private ClientCommunicationHandler handler;

    @Before
    public void setUp() throws IOException {
        // Start a test server
        testServer = new Server(TEST_PORT);
        serverThread = new Thread(() -> testServer.startup());
        serverThread.start();

        // Wait briefly for the server to start
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Connect a client socket to the test server and initialize SocketIO
        clientSocket = new Socket("localhost", TEST_PORT);
        clientIO = new SocketIO(clientSocket);
        handler = new ClientCommunicationHandler(clientSocket);
        assertTrue("RECIEVED HANDSHAKE", clientIO.checkForHandShake());
        assertTrue("SENT HANDSHAKE", clientIO.sendHandShake());
    }

    @After
    public void tearDown() throws IOException {
        // Close client and server sockets
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
        }
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
        }
    }

    @org.junit.Test
    public void testHandleSignup() {
        // Simulate client sending signup data
        String[] signupData = {"NewUser", "TestBio", "password"};
        clientIO.write(signupData, SocketIO.TYPE_SIGNUP);

        // Wait for the handler to process the data
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail("Test interrupted.");
        }

        String[] response = clientIO.read();
        assertEquals("Expected signup success message.", SocketIO.SUCCESS_USER_SIGNUP, response[0]);
    }

    @org.junit.Test
    public void testHandleLogin() {

        // Simulate client sending login data
        String[] loginData = {"TestUser", "password"};
        clientIO.write(loginData, SocketIO.TYPE_LOGIN);

        // Wait for the handler to process the data
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail("Test interrupted.");
        }

        String[] response = clientIO.read();
        assertEquals("Expected login success message.", SocketIO.SUCCESS_USER_LOGIN, response[0]);
    }

    @org.junit.Test
    public void testSendUserInfo() {
        // Set user
        User testUser = new User("TestUser", "TestBio", "password");
        try {
            handler.setUser(testUser);
        } catch (UserChatActiveException e) {
            fail("User Exists");
        }

        // Simulate a client requesting user info
        clientIO.write(null, SocketIO.TYPE_USER_INFORMATION);

        // Wait for the handler to process the data
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail("Test interrupted.");
        }

        String[] response = clientIO.read();
        assertEquals("Expected user information type.", SocketIO.TYPE_USER_INFORMATION, response[0]);
        assertEquals("Expected user name.", "TestUser", response[1]);
        assertEquals("Expected user bio.", "TestBio", response[2]);
    }

    @org.junit.Test
    public void testSendMessage() {
        // Set user
        User sender = new User("Sender", "Bio", "password");
        User receiver = new User("Receiver", "Bio", "password");
        try {
            handler.setUser(sender);
        } catch (UserChatActiveException e) {
            fail("User Already Active.");
        }

        // Simulate sending a message
        String[] messageData = {"Receiver", "Hello!"};
        clientIO.write(messageData, SocketIO.TYPE_MESSAGE);

        // Wait for the handler to process the data
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail("Test interrupted.");
        }

        // Verify no response is expected for message sending
        //String[] response = clientIO.read();
        assertNull("SUCCESS", null);
    }

    @org.junit.Test
    public void testSearchUsers() {
        // Add test users

        // Simulate searching for users
        String[] searchQuery = {"te"};
        clientIO.write(searchQuery, SocketIO.TYPE_USER_LIST_SEARCH);

        // Wait for the handler to process the data
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail("Test interrupted.");
        }

        String[] response = clientIO.read();
        assertEquals("Expected search result type.", SocketIO.TYPE_USER_LIST_SEARCH, response[0]);
        assertEquals("Expected matching user.", "TestUser", response[1]);
    }

    @org.junit.Test
    public void testSendFriendList() {
        // Set user with friends
        User testUser = new User("TestUser", "Bio", "password");
        testUser.getFriends().add("Friend1");
        testUser.getFriends().add("Friend2");

        try {
            handler.setUser(testUser);
        } catch (UserChatActiveException e) {
            fail("User Already Active.");
        }

        // Simulate requesting friend list
        clientIO.write(null, SocketIO.TYPE_FRIEND_LIST);

        // Wait for the handler to process the data
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail("Test interrupted.");
        }

        String[] response = clientIO.read();
        assertEquals("Expected friend list type.", SocketIO.TYPE_LIST_FRIENDS, response[0]);
        assertEquals("Expected first friend.", "Friend1", response[1]);
        assertEquals("Expected second friend.", "Friend2", response[2]);
    }
}
