//package tests;
//
//import static org.junit.Assert.*;
//import org.junit.Before;
//import org.junit.Test;
//import src.UpdateUsers;
//import java.net.ServerSocket;
//
//public class UpdateUsersTest {
//    private UpdateUsers updateUsers;
//    private ServerSocket serverSocket;
//
//    @Before
//    public void setUp() throws Exception {
//        serverSocket = new ServerSocket(8080);
//        updateUsers = new UpdateUsers(serverSocket);
//    }
//
//    @Test
//    public void testUpdateUsersConstructor() {
//        assertNotNull("UpdateUsers instance should be created", updateUsers);
//    }
//
//    @Test
//    public void testCurrentUsers() {
//        assertNotNull("Current users list should be initialized", UpdateUsers.currentUsers);
//    }
//
//    @Test
//    public void testUserNetMap() {
//        assertNotNull("User network map should be initialized", UpdateUsers.userNetMap);
//    }
//
//    @Test
//    public void testDatabaseIntegration() {
//        updateUsers.addUser("user1");
//        updateUsers.saveToDatabase();
//        assertTrue("User should be in database", mockDatabase.contains("user1"));
//    }
//}
