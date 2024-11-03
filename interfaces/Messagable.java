package interfaces;
import src.User;

import java.util.Date;

/**
 * The Messagable interface defines the necessary methods for handling message data.
 * Implementing classes should provide functionality for managing message content, sender, receiver,
 * storage details, and date, along with a structured format for storing and retrieving messages.
 */
public interface Messagable {

    /**
     * A unique separator used in formatted messages to delineate message sections.
     */
//    String MESSAGE_SEP = "Hyvp1VlujMNPHY9nySd25fxaluLUrI";
    String MESSAGE_SEP = "MESSAGE_SEP";
    /**
     * A unique string marking the end of a conversation, ensuring structured formatting in stored messages.
     */
//    String CONVO_END = "NIIztzeaegR12UzH1ra01BhKjQbHMH";
    String CONVO_END = "CONVO_END";

    /**
     * Generates a formatted string representation of the message for storage.
     * This should include all necessary message details, such as date, sender, content, and delimiters.
     *
     * @return A formatted string version of the message.
     */
    String toString();

    /**
     * Retrieves the text content of the message.
     *
     * @return The content of the message as a String.
     */
    String getContent();

    /**
     * Sets the text content of the message.
     *
     * @param content The new content for the message.
     */
    void setContent(String content);

    /**
     * Retrieves the file name used for storing this message, typically generated based on the sender
     * and receiver names.
     *
     * @return The file name or path as a String.
     */
    String getFileName();

    /**
     * Sets a new file name for storing the message.
     *
     * @param fileName The new file name or path.
     */
    void setFileName(String fileName);

    /**
     * Pushes the message content to a database or file for permanent storage.
     * This method should format and structure the message as needed for storage.
     * It should change the file represented by fileName to then add in the message contents
     * (See the toString() method)
     */
    void pushToDatabase();

    /**
     * Retrieves the User object representing the sender of the message.
     *
     * @return The sender of the message as a User object.
     */
    User getSender();

    /**
     * Sets a new sender for the message.
     *
     * @param user The new sender as a User object.
     */
    void setSender(User user);

    /**
     * Retrieves the User object representing the receiver of the message.
     *
     * @return The receiver of the message as a User object.
     */
    User getReceiver();

    /**
     * Sets a new receiver for the message.
     *
     * @param user The new receiver as a User object.
     */
    void setReceiver(User user);

    /**
     * Retrieves the date when the message was sent.
     *
     * @return The date of the message as a Date object.
     */
    Date getDate();

    /**
     * Sets the date when the message was sent.
     *
     * @param date The date the message was sent.
     */
    void setDate(Date date);
}
