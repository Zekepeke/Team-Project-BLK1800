package src.Server;

import src.*;
import src.Server.AuthenticationException.LoginException;
import src.Server.AuthenticationException.*;

import java.lang.Thread;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * The ConversationHandler class represents a thread that handles sole communication between the server and a single client.
 * This class handles signup, login, message parsing, and data delivery between the server and a client.
**/

public class ClientCommunicationHandler extends Thread {
    private ArrayList<String> availablefriends;
    private final Socket userSocket;
    private User user;

    private boolean userAlreadyActive(User user) {
        return false;
    }

    private User signup(String username, String bio, String password) {
        UserFileManager.writeNewUser(username);
        return new User(username, bio, password);
    }


    private User login(String username, String potentialPassword) throws LoginException {
        File f = new File("../USER_DATABASE/" + username + ".txt");

        if(!f.exists() || f.isDirectory()) {throw new LoginException(SocketIO.ERROR_USER_DNE);}

        try {
            User tempUser = new User(f.getPath());

            if(!potentialPassword.equals(tempUser.getPassword())) {
                throw new LoginException(SocketIO.ERROR_PASSWORD);
            }

            return tempUser;

        } catch (IOException e) {
            throw new LoginException("[ERROR] CANNOT READ USER DATA");
        }
    }

    /**
     * @param socket The socket to communicate between server and client
     */
    public ClientCommunicationHandler(Socket socket) {
        this.userSocket = socket;
    }


    public void setUser(User user) throws UserChatActiveException{
        for(Thread handler : ServerImplementation.activeConversations) {
            ClientCommunicationHandler clientCommunicationHandler = (ClientCommunicationHandler) handler;
            if(clientCommunicationHandler.getUser().getName().equals(user.getName())) {
                throw new UserChatActiveException();
            }
        }
    }

    public User getUser() {
        return this.user;
    }

    /*
     * Handles all states of communication
     */



    @Override
    public void run() {
        SocketIO messager = new SocketIO(this.userSocket);
        ServerState state = ServerState.SEND_HANDSHAKE;

        String[] dataFromClient = null;
        String informationType = null;
        UserFileManager.initialize();

        //Checking if the user is present or not
        while(!(this.userSocket.isClosed())){
            // Check for    client disconnection
            switch(state) {
                case SEND_HANDSHAKE:
                    messager.sendHandShake();
                    state = ServerState.READ_HANDSHAKE;
                    break;

                case READ_HANDSHAKE:
                    if(messager.checkForHandShake()) {
                        state = ServerState.READ_DATA;
                    } else{
                        System.out.println("Waiting for handshake from client");
                    }
                    break;

                case READ_DATA:
                    dataFromClient = messager.read();
                    if(dataFromClient == null) {
                        System.out.println("Waiting for data");
                    } else {
                        informationType = dataFromClient[0];
                        state = ServerState.EXECUTE;
                    }
                    break;
                case EXECUTE:

                    //trimming the data to remove the information index
                    String[] data = new String[dataFromClient.length - 1];

                    for(int i = 1; i < dataFromClient.length; i++) {
                        data[i - 1] = dataFromClient[i];
                    }

                    switch(informationType) {
                        case SocketIO.TYPE_SIGNUP:

                            if(UserFileManager.usernames.contains(data[0])) {
                                messager.writeCondition(SocketIO.ERROR_USER_EXISTS);
                                break;
                            }

                            try {
                                this.setUser(signup(data[0], data[1], data[2]));
                                messager.writeCondition(SocketIO.SUCCESS_USER_SIGNUP);
                            } catch (UserChatActiveException e) {
                                messager.writeCondition(e.getMessage());
                            }

                            break;

                        case SocketIO.TYPE_LOGIN:

                            try {
                                this.setUser(login(data[0], data[1]));
                                messager.writeCondition(SocketIO.SUCCESS_USER_LOGIN);

                            } catch(LoginException | UserChatActiveException e) {
                                messager.writeCondition(e.getMessage());
                            }

                            break;

                        case SocketIO.TYPE_USER_INFORMATION:
                            String[] sender = new String[]{this.user.getName(), this.user.getBio()};
                            messager.write(sender, SocketIO.TYPE_USER_INFORMATION);

                        case SocketIO.TYPE_MESSAGE:
                            try {
                                Message message = new Message(user, new User(dataFromClient[1] + ".txt"), null, dataFromClient[2]);
                                message.pushToDatabase();

                            } catch (IOException e) {
                                System.out.println("Idek");
                            }
                            break;

                        case SocketIO.TYPE_FRIEND_LIST:
                            ArrayList<String> friends = this.user.getFriends();
                            String[] friendList = new String[friends.size()];

                            for(int i = 0; i < friendList.length; i++) {
                                friendList[i] = friends.get(i);
                            }

                            messager.write(friendList, SocketIO.TYPE_LIST_FRIENDS);
                            break;
                        case SocketIO.TYPE_USER_LIST_SEARCH:

                            String query = data[0].toLowerCase();
                            ArrayList<String> matchingNames = new ArrayList<>();

                            for(String userName: UserFileManager.usernames) {
                                if(userName.toLowerCase().contains(query)){
                                    matchingNames.add(userName);
                                }
                            }

                            String[] sendUsers = matchingNames.toArray(new String[matchingNames.size()]);
                            messager.write(sendUsers, SocketIO.TYPE_USER_LIST_SEARCH);

                        case SocketIO.TYPE_FRIEND_CONVERSATION_HISTORY:

                            try {
                                User friendInformation = new User(data[0] + ".txt");

                                ConversationReader reader = new ConversationReader(this.user.getName(), friendInformation.getName());
                                ArrayList<Message> messages = reader.getMessages();
                                String[] messagesToString = new String[messages.size()];

                                for(int i = 0; i < messages.size(); i++) {
                                    messagesToString[i] = messages.get(i).toString();
                                }

                                messager.write(messagesToString, SocketIO.TYPE_FRIEND_CONVERSATION_HISTORY);

                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }

                            break;
                        default:
                            break;
                    }
            }
        }
    }
}
