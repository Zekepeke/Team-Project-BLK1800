    package src.Server;

    import Exceptions.DisconnectException;
    import interfaces.ClientHandlerInterface;
    import interfaces.IO;
    import src.*;
    import Exceptions.UserChatActiveException;
    import src.client.UserTransmission;

    import javax.swing.*;
    import java.lang.Thread;
    import java.util.ArrayList;
    import java.io.IOException;
    import java.net.Socket;
    import java.util.Arrays;
    import java.util.Date;

    /**
     * Handles communication between the server and a single client.
     * This includes signup, login, message parsing, and data delivery.
     */
    public class ClientCommunicationHandler extends Thread implements ClientHandlerInterface {
        private static Object o = new Object();
        int counter = 0;
        private final int THREAD_NUMBER;
        public enum State {
            SEND_HANDSHAKE,
            READ_HANDSHAKE,
            READ_DATA,
            EXECUTE
        }

        private final Socket userSocket;
        private final SocketIO messager;
        private User user;

        public boolean recreateUser() {
            try {
                this.user = new User(this.user.getName() + ".txt");
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        /**
         * Constructs a handler for client communication.
         *
         * @param socket The socket for server-client communication.
         */
        public ClientCommunicationHandler(Socket socket) {
            this.userSocket = socket;
            this.messager = new SocketIO(socket);
            THREAD_NUMBER = Server.threadCount;
            Server.threadCount++;
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

            if(count > 0) {
                throw new UserChatActiveException();
            } else {
                this.user = user;
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

            try {
                while (true) {

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
                            state = State.READ_DATA;
                            break;

                        default:
                            break;
                    }
                }
            } catch (DisconnectException e) {
                synchronized ( o ) {
                    System.out.println("---------------------------------------------------------");
                    System.out.println("Client " + this.THREAD_NUMBER + " terminated: " + e.getMessage());
                    System.out.println("---------------------------------------------------------");
                }
            }
        }

        /**
         * Processes the data sent by the client and performs corresponding actions.
         *
         * @param dataFromClient The data received from the client.
         */
        public void processClientData(String[] dataFromClient) throws DisconnectException {
            String informationType = dataFromClient[0];
            String[] data = new String[dataFromClient.length - 1];
            System.arraycopy(dataFromClient, 1, data, 0, dataFromClient.length - 1);
            boolean push_condition = false;
            synchronized ( o ) {
                System.out.println("---------------------------------------------------------");
                System.out.println("Client " + this.THREAD_NUMBER + " requests " + informationType);
                System.out.println("Data: " + String.join(" ", data));
                System.out.println("---------------------------------------------------------");
            }

            switch (informationType) {
                case SocketIO.TYPE_SIGNUP:
                    handleSignup(data);
                    break;

                case SocketIO.TYPE_LOGIN:
                    handleLogin(data);
                    break;
                case SocketIO.TYPE_UPDATE_USER_BIO:
                    updateUserBio(data);
                    push_condition = true;
                    break;
                case SocketIO.TYPE_USER_INFORMATION:
                    sendUserInfo();
                    break;

                case SocketIO.TYPE_MESSAGE:
                    sendMessage(data);
                    break;

                case SocketIO.TYPE_GET_MESSAGES_FROM_FRIEND:
                    getMessageData(data);
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

                case SocketIO.TYPE_UPDATE_BLOCKED_USERS:
                    push_condition = true;
                    updateBlockedUsers(data);
                    break;

                case SocketIO.TYPE_BLOCK_USER:
                    push_condition = true;
                    blockUnblockUser(data, SocketIO.TYPE_BLOCK_USER);
                    break;

                case SocketIO.TYPE_UNBLOCK_USER:
                    push_condition = true;
                    blockUnblockUser(data, SocketIO.TYPE_UNBLOCK_USER);
                    break;

                case SocketIO.TYPE_UNBLOCK_ALL_USERS:
                    push_condition = true;
                    blockUnblockUser(data, SocketIO.TYPE_UNBLOCK_ALL_USERS);
                    break;

                case SocketIO.TYPE_SEND_FRIEND_REQUEST:
                    push_condition = true;
                    sendFriendRequest(data);
                    break;

                case SocketIO.TYPE_ACCEPT_FRIEND_REQUEST:
                    push_condition = true;
                    acceptFriendRequest(data);
                    break;

                case SocketIO.TYPE_GET_INCOMING_FRIEND_REQUESTS:
                    sendIncomingFriendRequests();
                    break;

                case SocketIO.TYPE_GET_OUTGOING_FRIEND_REQUESTS:
                    sendOutgoingFriendRequests();
                    break;
                case SocketIO.TYPE_GET_BLOCKED_USERS:
                    getBlockedUsers();
                    break;
                case SocketIO.TYPE_IS_BLOCKED:
                    checkIsBlocked(data);
                    break;
                case SocketIO.TYPE_CHECK_EXCLUSIVE:
                    checkIfPeronIsFriendExclusive(data);
                    break;
                default:
                    break;
            }

            if(this.user != null && push_condition){
                if(!this.user.pushToDatabase()) {
                    throw new RuntimeException();
                }
            }
        }

        /**
         * Handles the signup process for a new user.
         *
         * @param data The data containing the username, bio, and password.
         */
        public void handleSignup(String[] data) throws DisconnectException {
            String name = data[0];
            String bio = "";
            String password = "";

            if(data.length < 3) {
             name = data[0];
             password = data[1];
            } else {
                bio = data[1];
                password = data[2];
            }

            if (User.userIsStored(name)) {
                messager.writeCondition(SocketIO.ERROR_USER_EXISTS);
                return;
            }

            try {
                // Create a new user and save to file
                User newUser;
                if(bio.isEmpty()) {
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
        public void handleLogin(String[] data) throws DisconnectException {
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
        public void sendUserInfo() throws DisconnectException {

            if(this.user == null) {
                messager.writeCondition(SocketIO.ERROR_USER_DNE);
                return;
            }

            String[] userInfo = new String[]{this.user.getName(), this.user.getBio()};
            messager.write(userInfo, SocketIO.TYPE_USER_INFORMATION);
        }

        /**
         * Sends a message to another user.
         *
         * @param data The data containing the recipient and the message.
         */
        public void sendMessage(String[] data) throws DisconnectException {
            try {
                Message message = new Message(user, new User(data[0] + ".txt"), new Date(), data[1]);
                message.pushToDatabase();
                messager.writeCondition(SocketIO.SUCCESS_GENERAL);
            } catch (IOException e) {
                messager.writeCondition(SocketIO.ERROR_IO_EXCEPTION);
                e.printStackTrace();
            }
        }

        public void getMessageData(String[] data) throws DisconnectException {
            String friendName = data[0];
            ConversationReader reader = new ConversationReader(this.user.getName(), friendName);
            ArrayList<Message> messages = reader.getMessages();
            String[] reps = new String[messages.size()];

            for(int i = 0; i < messages.size(); i++) {
                reps[i] = messages.get(i).toString();
            }

            System.out.println();
            for(String message : reps) {
                System.out.println(message);
            }

            messager.write(reps, SocketIO.TYPE_GET_MESSAGES_FROM_FRIEND);
        }

        /**
         * Sends the list of the current user's friends to the client.
         */
        public void sendFriendList() throws DisconnectException {
            recreateUser();
            ArrayList<String> friends = this.user.getFriends();
            if(friends == null) {
                messager.write(null, SocketIO.TYPE_GET_FRIEND_LIST);
                return;
            }
            ArrayList<String> transmissions = new ArrayList<>();
            for(String friend : friends) {
                try {
                    User user = new User(friend + ".txt");
                    transmissions.add(new UserTransmission(user.getName(), user.getBio(), user.getBlocked().contains(this.user.getName()), user.getExclusiveToFriends()).toString());
                } catch (IOException ignored) {

                }
            }
            messager.write(transmissions.toArray(new String[0]), SocketIO.TYPE_GET_FRIEND_LIST);
        }

        /**
             * Searches for users whose names match the query and sends the results to the client.
         *
         * @param data The data containing the search query.
         */
        public void searchUsers(String[] data) throws DisconnectException {
            String query = data[0].toLowerCase();
            ArrayList<String> matchingNames = new ArrayList<>();
            for (String userName : User.getUsernames()) {
                if (userName.toLowerCase().contains(query) && !userName.equals(this.user.getName())) {
                    try {
                        User user = new User(userName + ".txt");
                        matchingNames.add(new UserTransmission(user.getName(), user.getBio(), user.getBlocked().contains(this.user.getName()), user.getExclusiveToFriends()).toString());
                    } catch (IOException ignored) {

                    }
                }
            }
            messager.write(matchingNames.toArray(new String[0]), SocketIO.TYPE_USER_LIST_SEARCH);
        }

        /**
         * Sends the conversation history between the current user and a friend to the client.
         *
         * @param data The data containing the friend's username.
         */
        public void sendConversationHistory(String[] data) throws DisconnectException {
            recreateUser();
            try {
                User friend = new User(data[0] + ".txt");
                ConversationReader reader = new ConversationReader(this.user.getName(), friend.getName());
                ArrayList<Message> messages = reader.getMessages();
                messager.write(messages.stream().map(Message::toString).toArray(String[]::new),
                        SocketIO.TYPE_FRIEND_CONVERSATION_HISTORY);
            } catch (IOException e) {
                messager.writeCondition(SocketIO.ERROR_IO_EXCEPTION);
                e.printStackTrace();
            }
        }

        /**
         * Updates the list of blocked users sent by the client.
         *
         * @param data The data containing the list of new blocked users.
         */
        public void updateBlockedUsers(String[] data) throws DisconnectException {
            recreateUser();
            this.user.setBlocked(new ArrayList<>(Arrays.asList(data)));
            messager.writeCondition(SocketIO.SUCCESS_GENERAL);
        }

        public void getBlockedUsers() throws DisconnectException {
            recreateUser();
            ArrayList<String> blocked = this.user.getBlocked();
            if(blocked == null) {
                messager.write(null, SocketIO.TYPE_GET_BLOCKED_USERS);
                return;
            }
            ArrayList<String> transmissions = new ArrayList<>();
            for(String blockedUser : blocked) {
                try {
                    User user = new User(blockedUser + ".txt");
                    transmissions.add(new UserTransmission(user.getName(), user.getBio(), user.getBlocked().contains(this.user.getName()), user.getExclusiveToFriends()).toString());
                } catch (IOException ignored) {

                }
            }
            messager.write(transmissions.toArray(new String[0]), SocketIO.TYPE_GET_BLOCKED_USERS);
        }

        /**
         * Adds an outgoing friend request to the current user and updates incoming requests of recipient
         *
         * @param data The data containing the recipient's username.
         */
        public void sendFriendRequest(String[] data) throws DisconnectException {
            recreateUser();
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
                recipient.pushToDatabase();
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
        public void acceptFriendRequest(String[] data) throws DisconnectException {
            recreateUser();
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
                this.user.getFriendRequestsOut().remove(newFriend.getName());
                // updating the friends list of the person that sent the friend request (accepted their request)
                newFriend.getFriends().add(this.user.getName());
                newFriend.getFriendRequestsOut().remove(this.user.getName());
                newFriend.pushToDatabase();
                this.user.getFriendRequestsIn().remove(newFriend.getName());
                messager.writeCondition(SocketIO.SUCCESS_GENERAL);
            } else {
                messager.writeCondition(SocketIO.ERROR_GENERAL);
            }
        }

        /**
         * Sends the client a list of the incoming friend requests
         *
         */
        public void sendIncomingFriendRequests() throws DisconnectException {
            recreateUser();
            ArrayList<String> requests = this.user.getFriendRequestsIn();

            if(requests == null) {
                messager.write(null, SocketIO.TYPE_GET_INCOMING_FRIEND_REQUESTS);
                return;
            }
            ArrayList<String> transmissions = new ArrayList<>();
            for(String request : requests) {
                if(!this.user.getBlocked().contains(request)) {
                    try {
                        User user = new User(request + ".txt");
                        transmissions.add(new UserTransmission(user.getName(), user.getBio(), user.getBlocked().contains(this.user.getName()), user.getExclusiveToFriends()).toString());
                    } catch (IOException ignored) {

                    }
                }
            }
            messager.write(transmissions.toArray(new String[0]), SocketIO.TYPE_GET_INCOMING_FRIEND_REQUESTS);
            counter++;
        }

        /**
         * Sends the client a list of the outgoing friend requests
         *
         */
        public void sendOutgoingFriendRequests() throws DisconnectException {
            recreateUser();
            ArrayList<String> requests = this.user.getFriendRequestsOut();
            if(requests == null) {
                messager.write(null, SocketIO.TYPE_GET_OUTGOING_FRIEND_REQUESTS);
                return;
            }
            ArrayList<String> transmissions = new ArrayList<>();
            for(String request : requests) {
                try {
                    User user = new User(request + ".txt");
                    transmissions.add(new UserTransmission(user.getName(), user.getBio(), user.getBlocked().contains(this.user.getName()), user.getExclusiveToFriends()).toString());
                } catch (IOException ignored) {

                }
            }
            messager.write(transmissions.toArray(new String[0]), SocketIO.TYPE_GET_OUTGOING_FRIEND_REQUESTS);
        }

        public void blockUnblockUser(String[] data, String type) throws DisconnectException {
            recreateUser();
            String username = data[0];
            try {
                if(type.equals(SocketIO.TYPE_UNBLOCK_USER)) {
                    this.user.unblock(new User(username + ".txt"));
                }
                else if(type.equals(SocketIO.TYPE_BLOCK_USER)) {
                    this.user.block(new User(username + ".txt"));
                    this.user.getFriendRequestsIn().remove(username);
                    this.user.getFriendRequestsOut().remove(username);
                    this.user.getFriends().remove(username);
                }

                else if(type.equals(SocketIO.TYPE_UNBLOCK_ALL_USERS)) {
                    this.user.setBlocked(new ArrayList<>());
                }

                messager.writeCondition(SocketIO.SUCCESS_GENERAL);

            } catch (IOException e) {
                messager.writeCondition(SocketIO.ERROR_IO_EXCEPTION);
            }
        }

        public void updateUserBio(String[] data) {
            recreateUser();
            this.user.setBio(data[0]);
            messager.write(SocketIO.SUCCESS_GENERAL);
        }

        public void checkIsBlocked(String[] data) {
            recreateUser();
            String name = data[0];
            try {
                User user = new User(name + ".txt");
                if(user.getBlocked().contains(this.user.getName())) {
                    messager.write("true", SocketIO.TYPE_IS_BLOCKED);
                } else {
                    messager.write("false", SocketIO.TYPE_IS_BLOCKED);
                }
            } catch (IOException e) {
                messager.write(SocketIO.ERROR_USER_DNE);
            }
        }

        public void checkIfPeronIsFriendExclusive(String[] data){
            recreateUser();
            String name = data[0];
            try {
                User user = new User(name + ".txt");
                if(user.getExclusiveToFriends()) {
                    messager.write("true", SocketIO.TYPE_CHECK_EXCLUSIVE);
                } else {
                    messager.write("false", SocketIO.TYPE_CHECK_EXCLUSIVE);
                }
            } catch (IOException e) {
                messager.write(SocketIO.ERROR_USER_DNE);
            }
        }
    }
