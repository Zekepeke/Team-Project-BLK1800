package src.client;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientSide {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public ClientSide() {

    }



     public static void main(String[] args) throws Exception{
        // Create a client socket
        Socket s = new Socket(HOST, PORT);

        /*
        * TODO: MAKE A GUIForApp object
        * */
        GUIForApp guiForClient = GUIForApp();

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
        while (keepRunningUntilExit) {
            /*
            * TODO:Implement the GUI for the client side
            *
            *  */

            boolean userWantsToExist = guiForClient.exist;

            if (userWantsToExist) {
                keepRunningUntilExit = false;
                break;
            }
        }

        // close connection.
        dos.close();
        br.close();
        kb.close();
        s.close();

    }
}