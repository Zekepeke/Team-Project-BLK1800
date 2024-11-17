package src.server;

import interfaces.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        ServerImplementation server = new ServerImplementation(8282);
        server.startup();
    }

    /**
     *
     */
    public static class ServerImplementation implements Server {
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

        /**
         * Checks if the user is already active within another thread
         *
         *
         * @param userName
         * @return The reference to the existing user
         */

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
}
