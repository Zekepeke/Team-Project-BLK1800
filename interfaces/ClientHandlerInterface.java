package interfaces;

import src.User;
import Exceptions.UserChatActiveException;

/**
 * Interface for handling communication between the server and a single client.
 */
public interface ClientHandlerInterface {

    /**
     * Sets the current user and ensures no duplicate active sessions.
     *
     * @param user The User to set.
     * @throws UserChatActiveException If the user is already in an active session.
     */
    void setUser(User user) throws UserChatActiveException;

    /**
     * Gets the current user.
     *
     * @return The current User object.
     */
    User getUser();

    /**
     * Processes the data sent by the client and performs corresponding actions.
     *
     * @param dataFromClient The data received from the client.
     */
    void processClientData(String[] dataFromClient);

    /**
     * Handles the signup process for a new user.
     *
     * @param data The data containing the username, bio, and password.
     */
    void handleSignup(String[] data);

    /**
     * Handles the login process for an existing user.
     *
     * @param data The data containing the username and password.
     */
    void handleLogin(String[] data);

    /**
     * Sends the current user's information to the client.
     */
    void sendUserInfo();

    /**
     * Sends a message to another user.
     *
     * @param data The data containing the recipient and the message.
     */
    void sendMessage(String[] data);

    /**
     * Sends the list of the current user's friends to the client.
     */
    void sendFriendList();

    /**
     * Searches for users whose names match the query and sends the results to the client.
     *
     * @param data The data containing the search query.
     */
    void searchUsers(String[] data);

    /**
     * Sends the conversation history between the current user and a friend to the client.
     *
     * @param data The data containing the friend's username.
     */
    void sendConversationHistory(String[] data);
}