package interfaces;

import src.Message;

import java.util.ArrayList;

public interface ConversationReaderInterface {
    /**
     * Retrieves the list of messages in the conversation.
     * @return A list of messages.
     */
    ArrayList<Message> getMessages();

    /**
     * Adds a message to the conversation.
     * @param message The message to be added.
     */
    void addMessage(Message message);

    /**
     * Removes a message from the conversation.
     * @param message The message to be removed.
     * @return true if the message was removed successfully, false otherwise.
     */
    boolean removeMessage(Message message);
}