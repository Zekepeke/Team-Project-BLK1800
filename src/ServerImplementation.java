package src;

import interfaces.Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public class ServerImplementation implements Server {
    public ServerSocket serverSocket;

    public static ArrayList<Thread> activeConversations = new ArrayList<>();
    public static ArrayList<Socket> sockets = new ArrayList<>();
    public static ArrayList<User> activeUsers = new ArrayList<>();

    public static HashMap<String, Socket> userNetMap;


    /**
     * Constructs a new Server that clients can connect to, setting up users, client sockets, and
     * manages the different threads between each user.
     *
     * @param portnumber The specified port of the server client
     */
    private ServerImplementation(int portnumber) {
        try{
            serverSocket = new ServerSocket(portnumber);
        } catch(IOException e) {
            System.out.println("Unable to start server: " + e.getMessage());
        }

        try {
            this.userSocketUpdate.start();
        } catch(IllegalThreadStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the user is already active within another thread
     *
     *
     * @param userName
     * @return The reference to the existing user
     */
    public static User userExists(String userName) {
        for(User user : activeUsers) {
            if(user.getName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public boolean startup() {
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                sockets.add(clientSocket);
                activeConversations.add(new ConversersationHandler(clientSocket));
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
            for (int i = 0; i < ServerImplementation.activeConversations.size(); ) {
                if (activeConversations.get(i).isAlive()) {
                    activeConversations.remove(i);
                    sockets.remove(i);
                } else {
                    i++;
                }
            }
        }
    }
}
