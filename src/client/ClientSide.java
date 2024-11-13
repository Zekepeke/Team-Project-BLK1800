package src.client;
import java.io.*;
import java.net.*;

public class ClientSide {
    public final byte DELIMITER_LOGIN_BYTE = 0x01;
    public final byte DELIMITER_SIGNUP_BYTE = 0x02;
    public final byte DELIMITER_START_BYTE = 0x03;
    public final byte DELIMITER_END_BYTE = 0x04;
    public final byte SPLITTER_BYTE = 0x05;



    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    Socket userClient;

    public ClientSide() throws IOException {
        try {
            userClient = new Socket(HOST, PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * This method makes a new user and makes sure that the username and password is valid.
     * Username is valid is no special characters, lower case, no spaces, and should be in the bounds of 4 and 14.
     * Password is valid is no spaces and should be in the bounds of 4 and 14.
     *
     * @param username   The user making their own unique username
     * @param password The user making a password that is unique
     *
     * @return True if it is a valid sign-up and false if it is invalid
     */
    public boolean validSignUp(String username, String password) {
        boolean validUsername = username != null && !username.contains(" ") && username.length() >= 4 && username.length() <= 14;
        if (validUsername) {
            for (int i = 0; i < username.length(); i++) {
                char c = username.charAt(i);
                if ((!Character.isLetter(c) && !Character.isDigit(c)) || Character.isUpperCase(c)) {
                    validUsername = false;
                }
            }
        }
        boolean validPassword = password.length() >= 4 && password.length() <= 14 && !password.contains(" ");
        return validUsername && validPassword;

    }

    /**
     *
     * This is the first barrier for checking for a user
     * Username is valid is no special characters, lower case, no spaces, and should be in the bounds of 4 and 14.
     * Password is valid is no spaces and should be in the bounds of 4 and 14.
     *
     * @param username The name of a user already in the database
     * @param password The password of an existing user.
     *
     * @return True if the login was successful otherwise false
     */
    public boolean login(String username, String password) {



    }

    public static int getPORT() {
        return PORT;
    }

    public Socket getUserClient() {
        return userClient;
    }

    public static void main(String[] args) throws Exception{
        // Create a client socket
        ClientSide client = new ClientSide();

        PrintWriter sendToServer = new PrintWriter(client.getUserClient().getOutputStream(), true);
        BufferedReader readDataSentFromServer = new BufferedReader(new InputStreamReader(client.getUserClient().getInputStream()));

        ByteArrayOutputStream message = new ByteArrayOutputStream();
        OutputStream outputStream = client.getUserClient().getOutputStream();

        String user1Username = "somethingusername";
        String user1Password = "KSILOVER";

        String user2Username = "Bob";
        String user2Password = "password456";

        if (client.validSignUp(user1Username, user1Password)) {
            System.out.println("User registered successfully");
            sendToServer.println(user1Username);
            message.write(client.DELIMITER_SIGNUP_BYTE);
            message.write(client.DELIMITER_START_BYTE);
            message.write(user1Username.getBytes());
            message.write(client.SPLITTER_BYTE);
            message.write(user1Password.getBytes());
            message.write(client.DELIMITER_END_BYTE);

            outputStream.write(message.toByteArray());
            outputStream.flush();

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


        // Send data to the server
        DataOutputStream dos
                = new DataOutputStream(
                s.getOutputStream());

        // Read data coming from the server
        BufferedReader br
                = new BufferedReader(
                new InputStreamReader(
                        s.getInputStream()));

        // Read data from the keyboard
        BufferedReader kb
                = new BufferedReader(
                new InputStreamReader(System.in));
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

        // close connection.
        dos.close();
        br.close();
        kb.close();

    }


}