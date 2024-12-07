package src.client;
import Exceptions.BadClientException;
import interfaces.client.ClientSideInterface;
import src.SocketIO;
import src.User;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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

    private static Queue<RequestNode> requests = new LinkedList<>();
    private static boolean lock = false;

    public static ArrayList<String> blockedUsers = new ArrayList<>();
    public static ArrayList<String> outGoingFriendRequests = new ArrayList<>();
    public static String[] incomingFriendRequests = new String[0];

    private static boolean signedin = false;

    private static boolean write(String[] content, String type) {
        lock = true;
        requests.add(new RequestNode(content, type));
        lock = false;

        return true;
    }

    private static final String HOST = "localhost";
    private static final int PORT = 8282;
    private static SocketIO client = new SocketIO(HOST, PORT);

    Socket userClient;

    String username;
    String password;

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
        boolean validPassword = password.length() >= 3 && password.length() <= 14 && !password.contains(" ");

        if (validUsername) {
            for (int i = 0; i < username.length(); i++) {
                char c = username.charAt(i);
                if ((!Character.isLetter(c) && !Character.isDigit(c)) || Character.isUpperCase(c)) {
                    validUsername = false;

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
            String[] names = read();
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
    public Boolean searchNameAndPasswordLogin (String name, String password) {
        String[] stream = {name, password};
        if (client.write(stream, SocketIO.TYPE_LOGIN)) {
            String [] info = client.read();
            System.out.println("In the searchNameAndPasswordLogin Method:" + Arrays.toString(info));
            System.out.println("In the searchNameAndPasswordLogin Array size:" + info.length);
            String condition = info[0];

            if (condition == null) {
                return false;
            }

            switch (condition) {
                case SocketIO.ERROR_USER_DNE:
                    return null;
                case SocketIO.ERROR_PASSWORD:
                    return false;
                case SocketIO.SUCCESS_USER_LOGIN:
                    return true;
            }
        } else {
            return false;
        }
        return false;
    }

    // returns true if successfully signed up, false otherwise
    public Boolean searchNameAndPasswordSignUp (String name, String password){
        String[] stream = {name, password};
        if (client.write(stream, SocketIO.TYPE_SIGNUP)) {
            String [] info = client.read();
            System.out.println("In the searchNameAndPasswordSignUp Method:" + Arrays.toString(info));
            System.out.println("In the searchNameAndPasswordSignUp Array size:" + info.length);

            String condition = info[0];

            if (condition == null) {
                return null;
            }

            switch (condition) {
                case SocketIO.ERROR_USER_EXISTS:
                    return false;
                case SocketIO.SUCCESS_USER_SIGNUP:
                    return true;
            }
        } else {
            return false;
        }
        return false;
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

    public boolean getProfile() {
        boolean success = client.write(SocketIO.TYPE_USER_INFORMATION);
        String status = client.read()[0];
        return status.equals(SocketIO.SUCCESS_GENERAL);
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

    private boolean blockUser(String user) {
        blockedUsers.add(user);
        client.write(user, SocketIO.TYPE_BLOCK_USER);
        String status = client.read()[0];
        return status.equals(SocketIO.SUCCESS_GENERAL);
    }

    private boolean unblockUser(String user) {
        blockedUsers.remove(user);
        client.write(user, SocketIO.TYPE_UNBLOCK_USER);
        String status = client.read()[0];
        return status.equals(SocketIO.SUCCESS_GENERAL);
    }

    public static void logout() {
        incomingFriendRequests = new String[0];
        outGoingFriendRequests = new ArrayList<>();
        blockedUsers = new ArrayList<>();
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
                String default_send = SocketIO.TYPE_GET_INCOMING_FRIEND_REQUESTS;
                client.write(default_send);
                incomingFriendRequests = client.read();

                if (!lock && !requests.isEmpty()) {
                    RequestNode request = requests.remove();
                    String name = request.content[0];
                    switch (request.type) {
                        case SocketIO.TYPE_BLOCK_USER:
                            blockUser(name);
                            System.out.println("Block " + name + blockUser(name));
                            break;
                        case SocketIO.TYPE_SEND_FRIEND_REQUEST:
                            System.out.println("Send Request " + name + addFriend(name));
                            break;
                        case SocketIO.TYPE_UNBLOCK_USER:
                            System.out.println("Unblock user " + name + unblockUser(name));
                            break;
                        case SocketIO.TYPE_FRIEND_LIST:
                            System.out.println("Retrieve friend list: " + String.join(" ", listOfFriends()));
                    }
                }
            }
        }
    }
}