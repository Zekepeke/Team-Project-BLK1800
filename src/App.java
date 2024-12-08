package src;

import src.client.ClientSide;
import src.Server.Server;
import src.gui.pages.auth.AuthenticationPages;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class App {

//    public static void main (String[] args) throws IOException {
//        Server server = new Server(8282);
//        Socket socket = new Socket("localhost", 8282);
//        Socket socket1 = new Socket("localhost", 8282);
//        Socket socket2 = new Socket("localhost", 8282);
//        Socket socket3 = new Socket("localhost", 8282);
//
//        boolean success = server.startup();
//        if (!success) {
//            System.out.println("Failed to start server");
//        }
//        ClientSide client1 = new ClientSide(socket);
//        ClientSide client2 = new ClientSide(socket1);
//        ClientSide client3 = new ClientSide(socket2);
//        ClientSide client4 = new ClientSide(socket3);
//        String userBob = "bob";
//        String passwordBob = "password456";
//
//        String userAlice = "alice";
//        String passwordAlice = "password123";
//
//        String bobFriend = "bob's Friend";
//        String passworsBobFriend = "' Friend";
//
//        String userNew = "jake";
//        String passwordNew = "password123";
//
//        boolean[] arrOfUserAndPassword = new boolean[4];
//
//        arrOfUserAndPassword[0] = client1.validUserAndPassword(userBob, passwordBob);
//        arrOfUserAndPassword[1] = client2.validUserAndPassword(userAlice, passwordAlice);
//        arrOfUserAndPassword[2] = client3.validUserAndPassword(bobFriend, passworsBobFriend);
//        arrOfUserAndPassword[3] = client4.validUserAndPassword(userNew, passwordNew);
//        client1.sendHandShake();
//
//        // checking login works for server and client
//        if (client1.validUserAndPassword(userBob, passwordBob)) {
//            System.out.println("User registered successfully");
//            client1 = new ClientSide(socket, userBob, passwordBob);
//            // Sending if made a connection  with the sever
//            client1.sendHandShake();
//            // Sending login data to the server
//            boolean hasPassed = client1.write(
//                    new String [] {client2.getUsername(), client2.getPassword()},
//                    SocketIO.TYPE_LOGIN);
//
//            String conditionForUser = client2.readCondition();
//
//            if (hasPassed) {
//                if (conditionForUser != null) {
//                    System.out.println(conditionForUser);
//                } else if (conditionForUser.equals(SocketIO.SUCCESS_USER_LOGIN)) {
//                    System.out.println("User login was successfully");
//                } else if (conditionForUser.equals(SocketIO.ERROR_PASSWORD)) {
//                    System.out.println("Wrong password");
//                } else if (conditionForUser.equals(SocketIO.ERROR_USER_DNE)) {
//                    System.out.println("User does not exists");
//                }
//            } else {
//                System.out.println("User not registered successfully");
//            }
//        } else {
//            System.out.println("Username or password is incorrect");
//        }
//
//
//    }

    public static void main(String[] args) throws IOException {
        ClientSide clientHandler = new ClientSide();
        Thread clientHandlerThread = new Thread(clientHandler);
        //clientHandlerThread.start();
        SwingUtilities.invokeLater(new AuthenticationPages());
    }

}