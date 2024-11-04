package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.User;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUpClass() {
        user1 = new User("Alice", "Bio of Alice", "password123");
        user2 = new User("Bob", "Bio of Bob", "password456");
        user3 = new User("Charlie", "Bio of Charlie", "password789");
    }

    @Test
    void testConstructorWithoutBio() {
        User user = new User("David", "password321");
        assertEquals("David", user.getName());
        assertEquals("", user.getBio());
        assertEquals("password321", user.getPassword());
    }

    @Test
    void testSetName() {
        user1.setName("NewAlice");
        assertEquals("NewAlice", user1.getName());
    }

    @Test
    void testSetBio() {
        user1.setBio("Updated Bio");
        assertEquals("Updated Bio", user1.getBio());
    }

    @Test
    void testSetPassword() {
        user1.setPassword("newPassword123");
        assertEquals("newPassword123", user1.getPassword());
    }

    @Test
    void testSendFriendRequest() {
        assertTrue(user1.sendFriendRequest(user2));
        assertEquals(1, user1.getFriendRequestsOut().size());
        assertEquals(user2, user1.getFriendRequestsOut().get(0));
    }

    @Test
    void testSendFriendRequestAlreadyFriend() {
        user1.getFriends().add(user2);
        assertFalse(user1.sendFriendRequest(user2));
    }

    @Test
    void testSendFriendRequestBlockedUser() {
        user1.block(user2);
        assertFalse(user1.sendFriendRequest(user2));
    }

    @Test
    void testAcceptFriendRequest() {
        user2.getFriendRequestsIn().add(user1);
        assertTrue(user2.acceptFriendRequest(user1));
        assertEquals(1, user2.getFriends().size());
        assertEquals(user1, user2.getFriends().get(0));
    }

    @Test
    void testAcceptFriendRequestAlreadyFriend() {
        user2.getFriends().add(user1);
        assertFalse(user2.acceptFriendRequest(user1));
    }

    @Test
    void testAcceptFriendRequestBlockedUser() {
        user2.block(user1);
        assertFalse(user2.acceptFriendRequest(user1));
    }

    @Test
    void testBlockUser() {
        user1.block(user2);
        assertEquals(1, user1.getBlocked().size());
        assertEquals(user2, user1.getBlocked().get(0));
    }

    @Test
    void testUnblockUser() {
        user1.block(user2);
        assertTrue(user1.unblock(user2));
        assertEquals(0, user1.getBlocked().size());
    }

    @Test
    void testUnblockUserNotInBlockedList() {
        assertFalse(user1.unblock(user2));
    }

    @Test
    void testGetNumberOfFriends() {
        user1.getFriends().add(user2);
        user1.getFriends().add(user3);
        assertEquals(2, user1.getNumberOfFriends());
    }

    @Test
    void testGetNumberOfBlocked() {
        user1.block(user2);
        user1.block(user3);
        assertEquals(2, user1.getNumberOfBlocked());
    }

    @Test
    void testGetFriendRequestsIn() {
        user2.getFriendRequestsIn().add(user1);
        assertEquals(1, user2.getFriendRequestsIn().size());
        assertEquals(user1, user2.getFriendRequestsIn().get(0));
    }

    @Test
    void testGetFriendRequestsOut() {
        user1.getFriendRequestsOut().add(user2);
        assertEquals(1, user1.getFriendRequestsOut().size());
        assertEquals(user2, user1.getFriendRequestsOut().get(0));
    }
    @Test
    void testPushToDatabase() {
        System.out.println(user2.sendFriendRequest(user1));
        System.out.println(user2.sendFriendRequest(user1));
        user3.sendFriendRequest(user1);
        user3.sendFriendRequest(user2);
        user2.block(user3);
        System.out.println(user1.acceptFriendRequest(user2));
        System.out.println(user1.getFriendRequestsOut().size());
        user1.pushToDatabase();
        user2.pushToDatabase();
    }
}
