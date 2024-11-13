package src.server;
import src.UpdateUsers;
import src.User;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread;
import java.net.Socket;


public class ConversatorImplemenation  extends Thread {
    private ArrayList<String> availablefriends;
    private Socket userSocket;
    private String currentFriend;

    public ConversationImplemenation(User user) {
        availablefriends = user.getFriends();
        userSocket = UpdateUsers.userNetMap.get(user.getName());
    }   


    public String getConversatorUser() {
        return "";
    }


    @Override
    public void run() {
        //Creating a buffer to utilize the current user's socket
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));

        //Noting the current friend the client wants to talk to
        String currentFriend = getConversatorUser();

        //Checking if the user is present or not
        while(!(userSocket.isClosed())){
            // Check for client disconnection
            try {
                
                String inputMessage = inputStream.readLine();

            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
