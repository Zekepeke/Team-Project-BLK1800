package src.client;
import Exceptions.BadClientException;
import interfaces.client.ClientSideInterface;
import src.SocketIO;
import src.User;

import java.io.*;
import java.net.*;
import java.util.Arrays;
/**
 * Handles the client-side operations, including validating user credentials,
 * connecting to the server, and managing user-related functionalities like search,
 * profile, and friend list.
 *
 * This class extends the SocketIO class for socket communication
 * and implements clientSideInterface.
 */
public class ClientSide extends SocketIO implements ClientSideInterface {
    private static final String HOST = "localhost";
    private static final int PORT = 8282;
    Socket userClient;

    String username;
    String password;

    public ClientSide(Socket userClient, String username, String password) throws IOException {
        super(userClient);
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
        super(userClient);
        sendHandShake();
        if (checkForHandShake()) {
            System.out.println("Handshake successful");
        }
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
        boolean success = write(stream, TYPE_USER_LIST_SEARCH);
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
        if (write(stream, TYPE_LOGIN)) {
            String [] info = read();
            System.out.println("In the searchNameAndPasswordLogin Method:" + Arrays.toString(info));
            System.out.println("In the searchNameAndPasswordLogin Array size:" + info.length);
            String condition = info[0];

            if (condition == null) {
                return false;
            }

            switch (condition) {
                case ERROR_USER_DNE:
                    return null;
                case ERROR_PASSWORD:
                    return false;
                case SUCCESS_USER_LOGIN:
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
        if (write(stream, TYPE_SIGNUP)) {
            String [] info = read();
            System.out.println("In the searchNameAndPasswordSignUp Method:" + Arrays.toString(info));
            System.out.println("In the searchNameAndPasswordSignUp Array size:" + info.length);

            String condition = info[0];

            if (condition == null) {
                return null;
            }

            switch (condition) {
                case ERROR_USER_EXISTS:
                    return false;
                case SUCCESS_USER_SIGNUP:
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
        boolean success = write(stream, TYPE_USER_INFORMATION);
        if (success) {
            String [] info = read();
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
        boolean success = write(stream, TYPE_FRIEND_LIST);
        if (success) {
            String [] info = read();
            System.out.println(Arrays.toString(info));
            return info;
        }

        return null;
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




}