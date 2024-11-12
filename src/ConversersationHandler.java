package src;
import interfaces.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * The ConversationHandler class represents a thread that handles sole communication between the server and a single client.
 * This class handles signup, login, message parsing, and data delivery between the server and a client.
**/

public class ConversersationHandler extends Thread {
    private ArrayList<String> availablefriends;
    private Socket userSocket;
    private User user;

    /**
     * @param socket The socket to communicate between server and client
     */
    public ConversersationHandler(Socket socket) {
        this.userSocket = socket;
    }


    /**
     * Handles all states of communication
     */
    @Override
    public void run() throws ThreadTerminationException {

        SocketIO messager = new SocketIO(this.userSocket);
        ConversationHandlerState state = ConversationHandlerState.SEND_HANDSHAKE;
        String[] dataFromClient = null;
        byte informationType = 0x00;
        File f;

        //Checking if the user is present or not
        while(!(userSocket.isClosed())){
            // Check for client disconnection
            try {
                switch(state) {
                    case SEND_HANDSHAKE:
                        messager.sendHandShake();
                        state = ConversationHandlerState.READ_HANDSHAKE;
                        break;

                    case READ_HANDSHAKE:
                        dataFromClient = messager.read();
                        if(dataFromClient != null && messager.checkForHandShake(dataFromClient)) {
                            state = ConversationHandlerState.READ_DATA;
                        } else{
                            System.out.println("Waiting for handshake from client");
                        }
                        break;

                    case READ_DATA:
                        dataFromClient = messager.read();
                        if(dataFromClient == null) {
                            System.out.println("Waiting for data");
                        } else {
                            informationType = (byte)dataFromClient[0].charAt(0);
                            state = ConversationHandlerState.EXECUTE;
                        }
                        break;
                    case EXECUTE:
                        switch(informationType) {
                            case SocketIO.TYPE_BYTE_SIGNUP:
                                f = new File("../USER_DATABASE/" + dataFromClient[1] + ".txt");
                                if(f.exists() && !f.isDirectory()) { 
                                    messager.writeCondition(SocketIO.ERROR_BYTE_USER_EXISTS);
                                    break;
                                }

                                while((dataFromClient = messager.read()) != null && 
                                    dataFromClient[0].charAt(0) != SocketIO.TYPE_USER_SIGNUP_INFORMATION) {
                                    ;
                                }
                                
                                ServerImplementation.activeUsers.add(new User(dataFromClient[1], dataFromClient[2], dataFromClient[3]));
                                messager.writeCondition(SocketIO.SUCCESS_BYTE_USER_SIGNUP);

                                break;
                                
                            case SocketIO.TYPE_BYTE_LOGIN :
                                f = new File("../USER_DATABASE/" + dataFromClient[1] + ".txt");
                                if(!f.exists() || f.isDirectory()) { 
                                    messager.writeCondition(SocketIO.ERROR_BYTE_USER_DNE);
                                    break;
                                }

                                User temp1 = new User(f.getPath());
                                if(!dataFromClient[2].equals(temp1.getPassword())) {
                                    messager.writeCondition(SocketIO.ERROR_BYTE_PASSWORD);
                                    break;
                                }

                                User temp2 = ServerImplementation.userExists(temp1.getName());

                                if(temp2 != null){
                                    this.user = temp2;
                                } else {
                                    this.user = temp1;
                                }
                                messager.writeCondition(SocketIO.SUCCESS_BYTE_USER_LOGIN);

                                break;
                            case SocketIO.TYPE_BYTE_MESSAGE:
                                break;
                        }
                    }
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
        }
        throw new ThreadTerminationException();
    }
}
