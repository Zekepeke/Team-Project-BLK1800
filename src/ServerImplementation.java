package src;

import interfaces.Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerImplementation implements Server {
    public ServerSocket serverSocket;
    public ArrayList<User> currentUsers;
    public HashMap<User, Socket> userNetMap;
    private final Thread updateUserThread;

    private ServerImplementation(int portnumber) {
        try{
            serverSocket = new ServerSocket(portnumber);
        } catch(IOException e) {
            System.out.println("Unable to start server: " + e.getMessage());
        }
        
        this.updateUserThread = new Thread(new UpdateUsers(this.serverSocket));
        
        this.updateUserThread.start();

        this.currentUsers = UpdateUsers.currentUsers;
        this.userNetMap = UpdateUsers.userNetMap;
    }



}
