package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.*;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import src.ConversationReader;
import src.Message;
import src.User;
import java.lang.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class ConversationReaderTest {
    private static ConversationReader c;
    @Before
    public void setUp() throws Exception {
        c = new ConversationReader("Bob", "Alice");
    }

    @Test
    public void testGetMessages() {
        ArrayList<Message> messages = c.getMessages();
        assertNotNull(messages, "Messages list should not be null.");
    }

    @Test
    public void testAddAndRemoveMessage() {
        User u1 = new User("Joe","oof");
        User u2 = new User("John","oof");
        Message m = new Message(u1, u2,new Date(),"Heyyy");
        c.addMessage(m);
        ArrayList<Message> messages = c.getMessages();

        assertTrue(messages.contains(m),
                "The added message should be in the messages list.");
        boolean removed = c.removeMessage(m);

        assertTrue(removed, "The message should be removed successfully.");
        assertFalse(c.getMessages().contains(m), "Messages list should not contain the removed message.");
    }


    @Test
    public void testFileParsing() {
        // This test assumes that the MESSAGE_DATABASE/Alice-Bob.txt file exists and has valid content.
        // Add your test setup for any expected number of messages in the file if the file is present.
        ArrayList<Message> messages = c.getMessages();
        assertTrue(messages.size() > 0, "There should be messages parsed from the file.");
    }



}
