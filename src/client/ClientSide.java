package src.client;
import Exceptions.BadClientException;
import interfaces.client.ClientSideInterface;
import src.SocketIO;
import src.User;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Handles the client-side operations, including validating user credentials,
 * connecting to the server, and managing user-related functionalities like search,
 * profile, and friend list.
 *
 * This class extends the SocketIO class for socket communication
 * and implements clientSideInterface.
 */
public class ClientSide implements ClientSideInterface, Runnable {

    public enum State {
        PULL_DATA;
    }

    public static final int INVALID_PASSWORD = -1;
    public static final int USER_DNE = 0;
    public static final int USER_EXISTS = 1;
    public static final int SUCCESS = 2;
    public static final int ERROR = 3;

    private static Queue<RequestNode> requests = new LinkedList<>();

    private static boolean lock = false;

    public static List<String> blockedUsers = Collections.synchronizedList(new ArrayList<>());
    public static List<String> outGoingFriendRequests = Collections.synchronizedList(new ArrayList<>());
    public static List<String> userInformation = Collections.synchronizedList(new ArrayList<>());
    public static List<String> messageHistory = Collections.synchronizedList(new ArrayList<>());

    public static List<String>  friends = Collections.synchronizedList(new ArrayList<>());
    public static List<String>  incomingFriendRequests = Collections.synchronizedList(new ArrayList<>());
    public static AtomicBoolean friendExclusive = new AtomicBoolean();
    public static AtomicBoolean userBlocked = new AtomicBoolean();

    private static boolean signedin = false;


    private static final String HOST = "localhost";
    private static final int PORT = 8282;
    private static SocketIO client = new SocketIO(HOST, PORT);

    Socket userClient;

    public static String username;
    public static String password;
    public static String bio;

    public ClientSide(Socket userClient, String username, String password) throws IOException {
        try {
            if (validUserAndPassword(username, password)) {
                this.username = username;
                this.password = password;
            } else {
                throw new BadClientException("Invalid username or password");
            }
        } catch (BadClientException e) {
            e.printStackTrace();
            this.username = null;
            this.password = null;
        }

    }
    public ClientSide(Socket userClient) throws IOException {
        if (client.checkForHandShake()) {
            System.out.println("Handshake successful");
        }
        client.sendHandShake();
    }

    public ClientSide() {
        if (client.checkForHandShake()) {
            System.out.println("Handshake successful");
        }
        client.sendHandShake();
    }

    public static boolean command(String...content) {
        return command(false, content);
    }


    public static boolean command(boolean block, String[] content) {
        lock = true;
        String[] data = new String[content.length - 1];

        System.arraycopy(content, 1, data, 0, content.length - 1);

        requests.add(new RequestNode(data, content[content.length - 1]));

        lock = false;

        if(block) {
            while (!requests.isEmpty()) {
                System.out.println("Waiting for client request to complete");
            }
        }
        return true;
    }

    /**
     * This method makes a new user and makes sure that the username and password is valid.
     * Username is valid if no special characters, lower case, no spaces, and should be in the bounds of 4 and 14.
     * Password is valid if no spaces and should be in the bounds of 3 and 14.
     *
     * @param username The user making their own unique username
     * @param password The user making a password that is unique
     * @return True if it is a valid sign-up and false if it is invalid
     */

    public static boolean validUserAndPassword(String username, String password) {
        boolean validUsername = username != null && !username.contains(" ") && username.length() >= 3 && username.length() <= 14;
        boolean validPassword = password != null && password.length() >= 3 && password.length() <= 14 && !password.contains(" ");

        if (validUsername) {
            for (char c : username.toCharArray()) {
                if ((!Character.isLetter(c) && !Character.isDigit(c)) || Character.isUpperCase(c)) {
                    validUsername = false;
                    break;
                }
            }
        }
        return validUsername && validPassword;
    }

    /**
     * Attempts to check connection to the server by checking if the user client instance is initialized.
     *
     * @return {@code true} if the user client is not null, indicating a successful connection;
     *         {@code false} otherwise.
     */
    public boolean connectToServer() {
        return userClient != null;
    }


    /**
     * Searches for users matching the given name by communicating with the server.
     *
     * @param name the name to search for.
     * @return an array of usernames matching the search criteria if the operation is successful;
     *         {@code null} if the search fails or no results are found.
     *
     * @implNote Ensure the {@code SocketIO} class is updated so the search method
     *           works correctly and returns all the searched users.
     */

    public String[] search(String name){
        String[] stream = {name};
        boolean success = client.write(stream, SocketIO.TYPE_USER_LIST_SEARCH);
        if (success) {
            String[] names = client.read();
            if (names == null) {
                return null;
            }
            if (names.length == 1) {
                return names;
            } else {
                System.out.println("Something went wrong with array");
                System.out.println("searchUsers data sent back to client: " + Arrays.toString(names));
                return null;
            }
        }

        return null;
    }
    // returns true if logined, false otherwise
    public static int login (String name, String password) {
        String[] stream = {name, password};
        if (client.write(name, password, SocketIO.TYPE_LOGIN)) {

            String [] info = client.read();

            System.out.println("In the searchNameAndPasswordLogin Method:" + Arrays.toString(info));
            System.out.println("In the searchNameAndPasswordLogin Array size:" + info.length);

            String condition = info[0];

            if (condition == null) {
                return ERROR;
            }

            switch (condition) {
                case SocketIO.ERROR_USER_DNE:
                    return USER_DNE;
                case SocketIO.ERROR_PASSWORD:
                    return INVALID_PASSWORD;
                case SocketIO.SUCCESS_USER_LOGIN:
                    rungetAllFriends();
                    rungetProfile();
                    rungetIncomingFriendRequests();
                    getBlockedUsers();
                    signedin = true;
                    return SUCCESS;
            }
        }

        return ERROR;
    }

    // returns true if successfully signed up, false otherwise
    public static int signup(String name, String password){
        String[] stream = {name, password};
        if (client.write(stream, SocketIO.TYPE_SIGNUP)) {
            String [] info = client.read();
            System.out.println("In the searchNameAndPasswordSignUp Method:" + Arrays.toString(info));
            System.out.println("In the searchNameAndPasswordSignUp Array size:" + info.length);

            String condition = info[0];

            if (condition == null) {
                return ERROR;
            }

            switch (condition) {
                case SocketIO.ERROR_USER_EXISTS:
                    return USER_EXISTS;
                case SocketIO.SUCCESS_USER_SIGNUP:
                    signedin = true;
                    return SUCCESS;
            }
        }

        return ERROR;
    }

    /**
     * Retrieves the profile information of the current user by communicating with the server.
     *
     * @return an array of strings containing the user's profile information if the operation is successful;
     *         {@code null} if the request fails or no profile data is returned.
     */


    public String[] profile(){
        String [] stream = {"String"};
        boolean success = client.write(stream, SocketIO.TYPE_USER_INFORMATION);
        if (success) {
            String [] info = client.read();
            System.out.println(Arrays.toString(info));
            return info;
        }
        return null;
    }

    public User getUser() {
        try {
            return new User(username + ".txt");
        }
        catch (IOException e) {
            return null;
        }
    }

    /**
     * Retrieves the list of friends for the current user by communicating with the server.
     *
     * @return an array of strings containing the names or identifiers of the user's friends
     *         if the operation is successful; {@code null} if the request fails or no data is returned.
     *
     */
    public String[] listOfFriends(){
        String [] stream = {"String"};
        boolean success = client.write(stream, SocketIO.TYPE_FRIEND_LIST);
        if (success) {
            String [] info = client.read();
            System.out.println(Arrays.toString(info));
            return info;
        }
        return null;
    }




    private static boolean runsendMessage(String recipient, String message) {
        boolean success = client.write(recipient, message, SocketIO.TYPE_MESSAGE);
        String status = client.read()[0];
        return status.equals(SocketIO.SUCCESS_GENERAL);

    }

    private static boolean rungetMessageHistory(String person) {
        boolean success = client.write(person, SocketIO.TYPE_GET_MESSAGES_FROM_FRIEND);
        String[] input = client.read();
        messageHistory.clear();

        if(input.length > 1) {
            for (int i = 1; i < input[i].length(); i++) {
                messageHistory.add(input[i]);
            }
        } else {
            return false;
        }

        String status = input[0];
        return status.equals(SocketIO.SUCCESS_GENERAL);
    }

    private static boolean rungetProfile() {
        boolean success = client.write(SocketIO.TYPE_USER_INFORMATION);
        String[] input = client.read();
        userInformation.clear();
        userInformation.add(input[1]);
        userInformation.add(input[2]);
        String status = input[0];
        return status.equals(SocketIO.SUCCESS_GENERAL);
    }

    private static boolean rungetAllFriends() {
        client.write(SocketIO.TYPE_FRIEND_LIST);
        String[] input = client.read();

        if(input.length - 1 > friends.size()) {
            int counter = friends.size();
            while(friends.size() < input.length - 1) {
                friends.add(input[counter++]);
            }
        }
        if(input.length - 1 < friends.size()) {
            while(friends.size() > input.length - 1) {
                friends.remove(friends.size() - 1);
            }
        }

        String status = input[0];
        return status.equals(SocketIO.TYPE_FRIEND_LIST);
    }

    private static boolean runblockUser(String user) {
        blockedUsers.add(user);
        client.write(user, SocketIO.TYPE_BLOCK_USER);
        String status = client.read()[0];
        return status.equals(SocketIO.SUCCESS_GENERAL);
    }

    private static boolean rununblockUser(String user) {
        blockedUsers.remove(user);
        client.write(user, SocketIO.TYPE_UNBLOCK_USER);
        String status = client.read()[0];
        return status.equals(SocketIO.SUCCESS_GENERAL);
    }

    private static boolean runaddFriend(String user) {
        outGoingFriendRequests.add(user);
        client.write(user, SocketIO.TYPE_SEND_FRIEND_REQUEST);
        String status = client.read()[0];
        return status.equals(SocketIO.SUCCESS_GENERAL);
    }

    private static boolean rungetIncomingFriendRequests() {
        String default_send = SocketIO.TYPE_GET_INCOMING_FRIEND_REQUESTS;
        client.write(default_send);
        String[] input = client.read();

        if(input.length - 1 > incomingFriendRequests.size()) {
            int counter = incomingFriendRequests.size();
            while(incomingFriendRequests.size() < input.length - 1) {
                incomingFriendRequests.add(input[counter++]);
            }
        }
        if(input.length - 1 < incomingFriendRequests.size()) {
            while(incomingFriendRequests.size() > input.length - 1) {
                incomingFriendRequests.remove(incomingFriendRequests.size() - 1);
            }
        }

        String status = input[0];
        return status.equals(SocketIO.TYPE_GET_INCOMING_FRIEND_REQUESTS);
    }

    private static boolean getBlockedUsers() {
        client.write(SocketIO.TYPE_GET_BLOCKED_USERS);
        String[] input = client.read();
        if(input[0].equals(SocketIO.TYPE_FRIEND_LIST)) {
            String[] data = new String[input.length - 1];
            System.arraycopy(input, 1, data, 0, input.length - 1);
            blockedUsers.addAll(Arrays.asList(data));
            return true;
        } else {
            return false;
        }
    }

    private static boolean runupdateBio(String newBio) {
        boolean success = client.write(newBio, SocketIO.TYPE_UPDATE_USER_BIO);
        String[] input = client.read();
        userInformation.set(1, input[1]);
        String status = input[0];
        return status.equals(SocketIO.SUCCESS_GENERAL);
    }

    private static boolean runCheckBlocked(String name) {
        boolean success = client.write(name, SocketIO.TYPE_IS_BLOCKED);
        String[] input = client.read();
        if(input[0].equals(SocketIO.TYPE_IS_BLOCKED)) {
            userBlocked.set(Boolean.parseBoolean(input[1]));
            return true;
        } else {
            userBlocked = null;
            return false;
        }
    }

    private static boolean runCheckExclusive(String name) {
        boolean success = client.write(name, SocketIO.TYPE_CHECK_EXCLUSIVE);
        String[] input = client.read();
        if(input[0].equals(SocketIO.TYPE_CHECK_EXCLUSIVE)) {
            friendExclusive.set(Boolean.parseBoolean(input[1]));
            return true;
        } else {
            friendExclusive = null;
            return false;
        }
    }

    private static void runlogout() {
        incomingFriendRequests.clear();
        outGoingFriendRequests.clear();
        blockedUsers.clear();
        userInformation.clear();
        messageHistory.clear();
        signedin = false;
    }


    /**
     * Retrieves the port number used for the server connection.
     *
     * @return the port number as an integer.
     */
    public static int getPORT() {
        return PORT;
    }

    /**
     * Retrieves the socket instance representing the client connection to the server.
     *
     * @return the {@code Socket} object for the user client.
     */
    public Socket getUserClient() {
        return userClient;
    }

    /**
     * Retrieves the username of the current user.
     *
     * @return the username as a {@code String}.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password of the current user.
     *
     * @return the password as a {@code String}.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the username for the current user.
     *
     * @param username the new username as a {@code String}.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password for the current user.
     *
     * @param password the new password as a {@code String}.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    private void removeFriend(String name) {

    }


    @Override
    public void run() {
        while(true) {
            if (signedin) {
                if (!lock && !requests.isEmpty()) {
                    RequestNode request = requests.remove();
                    String name = request.content[0];
                    switch (request.type) {
                        case SocketIO.TYPE_MESSAGE:
                            runsendMessage(name, request.content[0]);
                        case SocketIO.TYPE_GET_MESSAGES_FROM_FRIEND:
                            rungetMessageHistory(name);
                        case SocketIO.TYPE_BLOCK_USER:
                            System.out.println("Block " + name + runblockUser(name));
                            break;
                        case SocketIO.TYPE_UPDATE_USER_BIO:
                            System.out.println("Update user bio: " + name + runupdateBio(name));
                        case SocketIO.TYPE_SEND_FRIEND_REQUEST:
                            System.out.println("Send friend Request " + name + runaddFriend(name));
                            break;
                        case SocketIO.TYPE_UNBLOCK_USER:
                            System.out.println("Unblock user " + name + rununblockUser(name));
                            break;
                        case SocketIO.TYPE_CHECK_EXCLUSIVE:
                            System.out.println("Unblock user " + name + runCheckExclusive(name));
                            break;
                        case SocketIO.TYPE_IS_BLOCKED:
                            System.out.println("Unblock user " + name + runCheckBlocked(name));
                            break;
                        case SocketIO.TYPE_GET_BLOCKED_USERS:
                            System.out.println("Got blocked users" + name + getBlockedUsers());
                    }
                }
                rungetAllFriends();
                rungetIncomingFriendRequests();
            }
        }
    }
}