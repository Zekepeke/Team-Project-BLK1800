package src.Server;

import interfaces.ClientHandlerInterface;
import src.*;
import Exceptions.UserChatActiveException;

import java.lang.Thread;
import java.util.ArrayList;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

/**
 * Handles communication between the server and a single client.
 * This includes signup, login, message parsing, and data delivery.
 */
public class ClientCommunicationHandler extends Thread implements ClientHandlerInterface {

    public enum State {
        SEND_HANDSHAKE,
        READ_HANDSHAKE,
        READ_DATA,
        EXECUTE
    }

    private final Socket userSocket;
    private final SocketIO messager;
    private User user;

    /**
     * Constructs a handler for client communication.
     *
     * @param socket The socket for server-client communication.
     */
    public ClientCommunicationHandler(Socket socket) {
        this.userSocket = socket;
        this.messager = new SocketIO(socket);
    }

    /**
     * Sets the current user and ensures no duplicate active sessions.
     *
     * @param user The User to set.
     * @throws UserChatActiveException If the user is already in an active session.
     */
    public void setUser(User user) throws UserChatActiveException {
        int count = 0;
        for(int i = 0; i < Server.activeUsers.size(); i++) {
            if(Server.activeUsers.get(i).getName().equals(this.user.getName())) {
                count++;
            }
        }

        if(count > 1) {
            throw new UserChatActiveException();
        }
    }

    /**
     * Gets the current user.
     *
     * @return The current User object.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Handles all states of server-client communication.
     */
    @Override
    public void run() {
        State state = State.SEND_HANDSHAKE;
        String[] dataFromClient = null;

        while (!this.userSocket.isClosed()) {
            switch (state) {
                case SEND_HANDSHAKE:
                    messager.sendHandShake();
                    state = State.READ_HANDSHAKE;
                    break;

                case READ_HANDSHAKE:
                    messager.checkForHandShake();
                    state = State.READ_DATA;
                    break;

                case READ_DATA:
                    dataFromClient = messager.read();
                    state = State.EXECUTE;
                    break;

                case EXECUTE:
                    processClientData(dataFromClient);
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * Processes the data sent by the client and performs corresponding actions.
     *
     * @param dataFromClient The data received from the client.
     */
    public void processClientData(String[] dataFromClient) {
        String informationType = dataFromClient[0];
        String[] data = new String[dataFromClient.length - 1];
        System.arraycopy(dataFromClient, 1, data, 0, dataFromClient.length - 1);

        switch (informationType) {
            case SocketIO.TYPE_SIGNUP:
                handleSignup(data);
                break;

            case SocketIO.TYPE_LOGIN:
                handleLogin(data);
                break;

            case SocketIO.TYPE_USER_INFORMATION:
                sendUserInfo();
                break;

            case SocketIO.TYPE_MESSAGE:
                sendMessage(data);
                break;

            case SocketIO.TYPE_FRIEND_LIST:
                sendFriendList();
                break;

            case SocketIO.TYPE_USER_LIST_SEARCH:
                searchUsers(data);
                break;

            case SocketIO.TYPE_FRIEND_CONVERSATION_HISTORY:
                sendConversationHistory(data);
                break;

            case SocketIO.TYPE_UPDATE_BLOCKED_USERS :
                updateBlockedUsers(data);

            case SocketIO.TYPE_SEND_FRIEND_REQUEST:
                sendFriendRequest(data);

            case SocketIO.TYPE_ACCEPT_FRIEND_REQUEST:
                acceptFriendRequest(data);
                break;

            case SocketIO.TYPE_GET_INCOMING_FRIEND_REQUESTS:
                sendIncomingFriendRequests();
                break;

            case SocketIO.TYPE_GET_OUTGOING_FRIEND_REQUESTS:
                sendOutgoingFriendRequests();
                break;

            default:
                break;
        }

        this.user.pushToDatabase();
    }

    /**
     * Handles the signup process for a new user.
     *
     * @param data The data containing the username, bio, and password.
     */
    public void handleSignup(String[] data) {
        String name = data[0];
        String bio = data[1];
        String password = data[2];

        if (User.userIsStored(name)) {
            messager.writeCondition(SocketIO.ERROR_USER_EXISTS);
            return;
        }

        try {
            // Create a new user and save to file
            User newUser;
            if(bio == null) {
                newUser = new User(name, password);
            } else {
                newUser = new User(name, bio, password);
            }
            this.setUser(newUser);
            messager.writeCondition(SocketIO.SUCCESS_USER_SIGNUP);
        } catch (UserChatActiveException e) {
            messager.writeCondition(e.getMessage());
        }
    }

    /**
     * Handles the login process for an existing user.
     *
     * @param data The data containing the username and password.
     */
    public void handleLogin(String[] data) {
        String name = data[0];
        String password = data[1];

        try {
            // Validate username and password
            if (!User.userIsStored(name)) {
                messager.writeCondition(SocketIO.ERROR_USER_DNE);
                return;
            }

            User tempUser = new User(name + ".txt");
            if (!password.equals(tempUser.getPassword())) {
                messager.writeCondition(SocketIO.ERROR_PASSWORD);
                return;
            }

            // Set the user and indicate success
            this.setUser(tempUser);
            messager.writeCondition(SocketIO.SUCCESS_USER_LOGIN);
        } catch (IOException e) {
            messager.writeCondition("[ERROR] CANNOT READ USER DATA");
        } catch (UserChatActiveException e) {
            messager.writeCondition(e.getMessage());
        }
    }

    /**
     * Sends the current user's information to the client.
     */
    public void sendUserInfo() {
        String name = this.user.getName();
        String bio = this.user.getBio();
        String[] userInfo = new String[]{this.user.getName(), this.user.getBio()};
        messager.write(userInfo, SocketIO.TYPE_USER_INFORMATION);
    }

    /**
     * Sends a message to another user.
     *
     * @param data The data containing the recipient and the message.
     */
    public void sendMessage(String[] data) {
        try {
            Message message = new Message(user, new User(data[0] + ".txt"), null, data[1]);
            message.pushToDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the list of the current user's friends to the client.
     */
    public void sendFriendList() {
        ArrayList<String> friends = this.user.getFriends();
        messager.write(friends.toArray(new String[0]), SocketIO.TYPE_GET_FRIEND_LIST);
    }

    /**
     * Searches for users whose names match the query and sends the results to the client.
     *
     * @param data The data containing the search query.
     */
    public void searchUsers(String[] data) {
        String query = data[0].toLowerCase();
        ArrayList<String> matchingNames = new ArrayList<>();
        for (String userName : User.getUsernames()) {
            if (userName.toLowerCase().contains(query)) {
                matchingNames.add(userName);
            }
        }
        messager.write(matchingNames.toArray(new String[0]), SocketIO.TYPE_USER_LIST_SEARCH);
    }

    /**
     * Sends the conversation history between the current user and a friend to the client.
     *
     * @param data The data containing the friend's username.
     */
    public void sendConversationHistory(String[] data) {
        try {
            User friend = new User(data[0] + ".txt");
            ConversationReader reader = new ConversationReader(this.user.getName(), friend.getName());
            ArrayList<Message> messages = reader.getMessages();
            messager.write(messages.stream().map(Message::toString).toArray(String[]::new),
                    SocketIO.TYPE_FRIEND_CONVERSATION_HISTORY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the list of blocked users sent by the client.
     *
     * @param data The data containing the list of new blocked users.
     */
    public void updateBlockedUsers(String[] data) {
        this.user.setBlocked(new ArrayList<>(Arrays.asList(data)));
    }

    /**
     * Adds an outgoing friend request to the current user and updates incoming requests of recipient
     *
     * @param data The data containing the recipient's username.
     */
    public void sendFriendRequest(String[] data) {
        String name = data[0];
        User recipient;

        try {
            recipient = new User(name + ".txt");
        } catch (IOException e) {
            messager.writeCondition(SocketIO.ERROR_IO_EXCEPTION);
            return;
        }

        // updating the outgoing friend request list of the current user
        if(this.user.sendFriendRequest(recipient)) {
            // updating the incoming friend request list of the person that sent the friend request
            recipient.getFriendRequestsIn().add(this.user.getName());
            messager.writeCondition(SocketIO.SUCCESS_GENERAL);
        } else {
            messager.writeCondition(SocketIO.ERROR_GENERAL);
        }

    }

    /**
     * Accepts friend request of the specified user and updates the specified user's friend list.
     *
     * @param data The data containing the specified user's username.
     */
    public void acceptFriendRequest(String[] data) {
        String name = data[0];
        User newFriend;

        try {
            newFriend = new User(name + ".txt");
        } catch (IOException e) {
            messager.writeCondition(SocketIO.ERROR_IO_EXCEPTION);
            return;
        }

        // accepting the friend request sent by another user
        if(this.user.acceptFriendRequest(newFriend)) {

            // updating the friends list of the person that sent the friend request (accepted their request)
            newFriend.getFriends().add(this.user.getName());
            messager.writeCondition(SocketIO.SUCCESS_GENERAL);
        } else {
            messager.writeCondition(SocketIO.ERROR_GENERAL);
        }
    }

    /**
     * Sends the client a list of the incoming friend requests
     *
     */
    public void sendIncomingFriendRequests() {
        messager.write(this.user.getFriendRequestsIn().toArray(new String[0]), SocketIO.TYPE_GET_INCOMING_FRIEND_REQUESTS);
    }

    /**
     * Sends the client a list of the outgoing friend requests
     *
     */
    public void sendOutgoingFriendRequests() {
        messager.write(this.user.getFriendRequestsOut().toArray(new String[0]), SocketIO.TYPE_GET_OUTGOING_FRIEND_REQUESTS);
    }

}