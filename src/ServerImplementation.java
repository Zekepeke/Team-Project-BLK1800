package src;

import interfaces.Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerImplementation implements Server {
    public ServerSocket serverSocket;
    public ArrayList<Socket> userSockets;
    private ArrayList<ConversersationHandler> activeConversations;

    public static HashMap<String, Socket> userNetMap;
    public static ArrayList<User> activeUsers = new ArrayList<>();

    private final Thread userSocketUpdate;

    public final byte PACKET_OPEN = 0x2;
    public final byte PACKET_CLOSE = 0x3;
    public final byte SEPERATOR = 0x4;

    //Reading in message data from client, including recipient name, and message content.
    private ServerImplementation(int portnumber) {
        try{
            serverSocket = new ServerSocket(portnumber);
        } catch(IOException e) {
            System.out.println("Unable to start server: " + e.getMessage());
        }
        
        this.userSocketUpdate = new Thread(new ConnectionManager(this.serverSocket));
        this.activeConversations = new ArrayList<>();

        try {
            this.userSocketUpdate.start();
        } catch(IllegalThreadStateException e) {
            e.printStackTrace();
        }

        this.userSockets = ConnectionManager.sockets;
    }

    public static User userExists(String userName) {
        for(User user : activeUsers) {
            if(user.getName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public boolean startup() {
        int currentUserSize = ConnectionManager.userSize;
        while (true) { 
            if(ConnectionManager.userSize > currentUserSize) {
                currentUserSize = ConnectionManager.userSize;
                activeConversations.add(new ConversersationHandler(ConnectionManager.sockets.get(currentUserSize - 1)));
                activeConversations.get(currentUserSize - 1).start();
            }
        }
    }
}
