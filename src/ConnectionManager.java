package src;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class ConnectionManager implements Runnable {
    public static ArrayList<Socket> sockets = new ArrayList<>();
    public static int userSize = 0;

    public static HashMap<String, Socket> userNetMap = new HashMap<>();
    public ServerSocket serverSocket;
    

    public ConnectionManager(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                    sockets.add(clientSocket);
                    userSize++;
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}