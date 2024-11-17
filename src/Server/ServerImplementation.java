package src.server;

import interfaces.Server;
import src.User;
import src.server.ClientCommunicationHandler;

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



    /**
     * Constructs a new Server that clients can connect to, setting up users, client sockets, and
     * manages the different threads between each user.
     *
     * @param portnumber The specified port of the server client
     */
    public ServerImplementation(int portnumber) {
        try{
            serverSocket = new ServerSocket(portnumber);
        } catch(IOException e) {
            System.out.println("Unable to start server: " + e.getMessage());
        }
    }


    public boolean startup() {
        while (true) {

            //The try statement checks if another server has connected, initializing a new conversation thread
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                sockets.add(clientSocket);
                Thread handler = new ClientCommunicationHandler(clientSocket);
                activeConversations.add(handler);
                handler.start();

            } catch(IOException e) {
                System.out.println(e.getMessage());
            }

            //Checks for any disconnected users, terminating the conversation thread
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