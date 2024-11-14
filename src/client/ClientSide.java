package src.client;
import Exceptions.BadClientException;
import src.SocketIO;

import java.io.*;
import java.net.*;

public class ClientSide extends SocketIO {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
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
        }

    }
    public ClientSide(Socket userClient) throws IOException {
        super(userClient);
    }

    /**
     * This method makes a new user and makes sure that the username and password is valid.
     * Username is valid if no special characters, lower case, no spaces, and should be in the bounds of 4 and 14.
     * Password is valid if no spaces and should be in the bounds of 4 and 14.
     *
     * @param username The user making their own unique username
     * @param password The user making a password that is unique
     * @return True if it is a valid sign-up and false if it is invalid
     */
    public boolean validUserAndPassword(String username, String password) {
        boolean validUsername = username != null && !username.contains(" ") && username.length() >= 4 && username.length() <= 14;
        boolean validPassword = password.length() >= 4 && password.length() <= 14 && !password.contains(" ");

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

    public void search(){
        /*
         * TODO: After the socketIO class is updated make sure the search method works where it returns all the searched users
         *
         * */

    }
    public void profile(){
        /*
         * TODO: After the socketIO class is updated make sure to get the info of the clients profile including bio and possibly pfp and return the list
         *
         * */

    }
    public String[] listOfFriends(){
        /*
         * TODO: After the socketIO class is updated make sure to get the list of friends from the user and return the list
         *
         *
         * */
        return null;
    }



    public static int getPORT() {
        return PORT;
    }

    public Socket getUserClient() {
        return userClient;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }





    public static void main(String[] args) throws Exception{
        // Create a client socket
        Socket userClient = new Socket(HOST, PORT);


        String user1Username = "somethingusername";
        String user1Password = "KSILOVER";
        ClientSide client1 = new ClientSide(userClient);

        String user2Username = "Bob";
        String user2Password = "password456";
        ClientSide client2 = new ClientSide(userClient);

        String user3Username = "Johnis∑&Cool";
        String user3Password = "password456";
        ClientSide client3 = new ClientSide(userClient);

        if (client3.validUserAndPassword(user3Username, user3Password)) {
            System.out.println("valid user and password");
        } else {
            System.out.println("invalid user and password");
        }



        if (client1.validUserAndPassword(user1Username, user1Password)) {
            System.out.println("User registered successfully");
            client1 = new ClientSide(userClient, user1Username, user1Password);
            // Sending if made a connection  with the sever
            client1.sendHandShake();
            // Sending Signup data to the server
            client1.write(
                    new String [] {client1.getUsername(), client1.getPassword()},
                    SocketIO.TYPE_USER_SIGNUP_INFORMATION);

            byte conditionForUser = client1.readCondition();
            switch (conditionForUser) {
                case SocketIO.SUCCESS_BYTE_USER_SIGNUP:
                    System.out.println("User sign up was successfully");
                    break;
                case SocketIO.ERROR_BYTE_USER_EXISTS:
                    System.out.println("Wrong password");
                    break;
            }


        } else {
            System.out.println("Username or password is incorrect");
        }

        if (client2.validUserAndPassword(user2Username, user2Password)) {
            System.out.println("User registered successfully");
            client2 = new ClientSide(userClient, user2Username, user2Password);
            // Sending if made a connection  with the sever
            client2.sendHandShake();
            // Sending login data to the server
            client2.write(
                    new String [] {client2.getUsername(), client2.getPassword()},
                    SocketIO.TYPE_USER_LOGIN_INFORMATION);

            byte conditionForUser = client2.readCondition();
            switch (conditionForUser) {
                case SocketIO.SUCCESS_BYTE_USER_LOGIN:
                    System.out.println("User logged in successfully");
                    break;
                case SocketIO.ERROR_BYTE_PASSWORD:
                    System.out.println("Wrong password");
                    break;
                case SocketIO.ERROR_BYTE_USER_DNE:
                    System.out.println("User does not exist");
                    break;
            }
        } else {
            System.out.println("Username or password is incorrect");
        }







        /*
         * TODO: Make sure to read in the data from the user and sent it to the user
         *
         * */


        /*
         * TODO: MAKE A GUIForApp object
         * */


        boolean keepRunningUntilExit = true;

        // Repeat as long as exit Is not typed at client
//        while (keepRunningUntilExit) {
//            /*
//             * TODO:Implement the GUI for the client side
//             *
//             *  */
//
//            boolean userWantsToExist = guiForClient.exist;
//
//            if (userWantsToExist) {
//                keepRunningUntilExit = false;
//                break;
//            }
//        }

    }


}