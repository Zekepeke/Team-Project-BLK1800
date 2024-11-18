package src.Server;

import interfaces.ServerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The ServerInterface class represents a server that manages client connections,
 * user communication, and threads for each active conversation.
 */
public class Server implements ServerInterface {
    private static final int PORT_NUMBER = 8282;
    private final ServerSocket serverSocket;

    // Stores active conversation threads
    public static final ArrayList<Thread> activeConversations = new ArrayList<>();
    // Stores client sockets
    public static final ArrayList<Socket> sockets = new ArrayList<>();

    /**
     * Constructs a new server to handle client connections and communication.
     *
     * @param portNumber The port number the server will listen on.
     */
    public Server(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("ServerInterface started on port " + portNumber);
        } catch (IOException e) {
            throw new RuntimeException("Unable to start server: " + e.getMessage());
        }
    }

    /**
     * Starts the server, continuously listening for client connections and managing active threads.
     *
     * @return true if the server starts successfully, otherwise false.
     */
    public boolean startup() {
        System.out.println("ServerInterface is running...");
        while (true) {
            try {
                // Accept incoming client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                sockets.add(clientSocket);

                // Create and start a new thread for the client
                ClientCommunicationHandler handler = new ClientCommunicationHandler(clientSocket);
                Thread thread = new Thread(handler);
                activeConversations.add(thread);
                thread.start();
            } catch (IOException e) {
                System.err.println("Error while accepting a client connection: " + e.getMessage());
            }

            // Remove disconnected users and terminate inactive threads
            cleanUpInactiveConversations();
        }
    }

    /**
     * Removes inactive threads and their corresponding sockets from the active lists.
     */
    private void cleanUpInactiveConversations() {
        for (int i = 0; i < activeConversations.size(); ) {
            Thread thread = activeConversations.get(i);
            if (!thread.isAlive()) {
                System.out.println("Removing inactive conversation...");
                activeConversations.remove(i);
                sockets.remove(i);
            } else {
                i++;
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(PORT_NUMBER);
        server.startup();
    }
}
