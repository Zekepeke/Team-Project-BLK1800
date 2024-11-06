package src;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class UpdateUsers implements Runnable {
    public static ArrayList<User> currentUsers = new ArrayList<>();
    public static HashMap<User, Socket> userNetMap = new HashMap<>();
    public ServerSocket serverSocket;
    

    public UpdateUsers(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try (java.net.Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                //reading in the initial User data
                ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());
                
                try {
                    User user = (User)(is.readObject());
                    //
                    currentUsers.add(user);
                    userNetMap.put(user, clientSocket);
                    System.out.println("Client " + user.getName() + " added!");

                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}