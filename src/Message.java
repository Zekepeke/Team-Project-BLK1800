package src;
import interfaces.Messagable;
import java.io.*;
import java.util.*;


/**
 * The Message class represents a message sent from one user to another, including details such as the sender, receiver,
 * content, date, and file name for storage. It implements the Messagable interface, allowing for interaction with
 * message content, date, sender, receiver, and file storage paths.
 */
public class Message implements Messagable {
    User sender;
    User receiver;
    String fileName;
    String content;
    Date date;

    /**
     * Constructs a new Message with specified sender, receiver, date, and content.
     * The file name for storage is automatically generated based on the sender and receiver names.
     *
     * @param sender   The user sending the message
     * @param receiver The user receiving the message
     * @param date     The date and time the message was sent
     * @param content  The text content of the message
     */
    public Message(User sender, User receiver, Date date, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
        this.content = content;
        if (sender.getName().compareTo(receiver.getName()) < 0) {
            fileName = sender.getName() + "-" + receiver.getName();
        } else {
            fileName = receiver.getName() + "-" + sender.getName();
        }
        fileName += ".txt";
    }

    /**
     * Default constructor for the Message class, initializes an empty message with no content, sender, or receiver.
     */
    public Message() {}

    /**
     * Retrieves the content of this message.
     *
     * @return The content of the message as a String
     */
    @Override
    public synchronized String getContent() {
        return this.content;
    }

    /**
     * Sets the content of this message to the specified text.
     *
     * @param content The new content of the message
     */
    @Override
    public synchronized void setContent(String content) {
        this.content = content;
    }

    /**
     * Retrieves the file name used to store the message, based on the sender and receiver names.
     *
     * @return The file path or name where the message is stored
     */
    @Override
    public synchronized String getFileName() {
        return fileName;
    }

    /**
     * Sets a new file name for storing the message.
     *
     * @param fileName The new file name or path where the message will be stored
     */
    @Override
    public synchronized void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Pushes the message data to a database, using the file name for storage.
     * The method applies separators to structure the message data consistently.
     */
    @Override
    public synchronized void pushToDatabase() {
        ArrayList<String> a = new ArrayList<>();
        try (BufferedReader b= new BufferedReader(new FileReader(fileName));) {
            while (true) {
                String s = b.readLine();
                if (s == null){
                    break;
                }
                if (s.equals(CONVO_END)) {
                    continue;
                }
                a.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (PrintWriter p= new PrintWriter(new FileWriter(fileName));) {
            for (String s : a) {
                p.println(s);
            }
            p.println(this.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provides a formatted string representation of the message, designed for storage in a text file.
     *
     * The structure of the formatted message is as follows:
     * 1) Message separator
     * 2) Date sent
     * 3) Sender's name
     * 4) Message content
     * 5) Conversation end string (30 characters)
     *
     * @return A String representing the formatted message for the database
     */
    public synchronized String toString() {
        return MESSAGE_SEP + "\n" + date.toString() + "\n" +
                sender.getName() + "\n" + content + "\n" + CONVO_END;
    }

    /**
     * Retrieves the User object representing the sender of the message.
     *
     * @return The sender of the message as a User object
     */
    @Override
    public synchronized User getSender() {
        return this.sender;
    }

    /**
     * Sets the sender of the message to the specified user.
     *
     * @param user The new sender as a User object
     */
    @Override
    public synchronized void setSender(User user) {
        this.sender = user;
    }

    /**
     * Retrieves the User object representing the receiver of the message.
     *
     * @return The receiver of the message as a User object
     */
    @Override
    public synchronized User getReceiver() {
        return this.receiver;
    }

    /**
     * Sets the receiver of the message to the specified user.
     *
     * @param user The new receiver as a User object
     */
    @Override
    public synchronized void setReceiver(User user) {
        this.receiver = user;
    }

    /**
     * Retrieves the date when the message was sent.
     *
     * @return The date of the message as a Date object
     */
    @Override
    public synchronized Date getDate() {
        return date;
    }

    /**
     * Sets the date when the message was sent.
     *
     * @param date The new date for the message
     */
    @Override
    public synchronized void setDate(Date date) {
        this.date = date;
    }
}
