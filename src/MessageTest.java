package src;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.file.Path;
import java.util.Date;

public class MessageTest {
    private static User sender;
    private static User receiver;
    private static Date date;
    private static Message message;

    @BeforeClass
    public static void setUp() {
        sender = new User("Alice", "ohNoBoat123!");
        receiver = new User("Bob","bigBoat123!");
        date = new Date();
        message = new Message(sender, receiver, date, "Hello, Bob!");
    }

    @Test
    public void testConstructorAndFileName() {
        // Test file name generation based on sender and receiver names
        assertEquals("Alice@Bob", message.getFileName());

        // Test message content, sender, receiver, and date
        assertEquals("Hello, Bob!", message.getContent());
        assertEquals(sender, message.getSender());
        assertEquals(receiver, message.getReceiver());
        assertEquals(date, message.getDate());
    }

    @Test
    public void testSetContent() {
        message.setContent("New content");
        assertEquals("New content", message.getContent());
    }

    @Test
    public void testSetFileName() {
        message.setFileName("NewFileName.txt");
        assertEquals("NewFileName.txt", message.getFileName());
    }

    @Test
    public void testToStringFormat() {
        String expectedString = Message.MESSAGE_SEP + "\n" +
                date.toString() + "\n" +
                sender.getName() + "\n" +
                "Hello, Bob!\n" + Message.CONVO_END;
        assertEquals(expectedString, message.toString());
    }

    @Test
    public void testSetAndGetSender() {
        User newSender = new User("Charlie", "bigGoat123");
        message.setSender(newSender);
        assertEquals(newSender, message.getSender());
    }

    @Test
    public void testSetAndGetReceiver() {
        User newReceiver = new User("David", "bigGoat123");
        message.setReceiver(newReceiver);
        assertEquals(newReceiver, message.getReceiver());
    }

    @Test
    public void testSetAndGetDate() {
        Date newDate = new Date();
        message.setDate(newDate);
        assertEquals(newDate, message.getDate());
    }

    @Test
    public void testPushToDatabase() throws IOException {
        // Temporarily set the file name to the temp directory path
        File file = new File("testFile.txt");
        message.setFileName(file.getAbsolutePath());

        // Write initial message to file
        message.pushToDatabase();

        // Verify file contents
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            assertEquals(Message.MESSAGE_SEP, reader.readLine());
            assertEquals(date.toString(), reader.readLine());
            assertEquals(sender.getName(), reader.readLine());
            assertEquals("Hello, Bob!", reader.readLine());
            assertEquals(Message.CONVO_END, reader.readLine());
        }
    }
}
