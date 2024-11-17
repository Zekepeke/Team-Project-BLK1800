package interfaces.client;
/**
 * Defines the contract for client-side operations, including user authentication,
 * server connection, and handling user-specific features like searching,
 * retrieving profiles, and listing friends.
 */
public interface clientSideInterface {
    static final String HOST = "localhost";
    static final int PORT = 5000;
    boolean validUserAndPassword(String username, String password);
    boolean connectToServer();
    String[] search(String name);
    String[] profile();
    String[] listOfFriends();


    public static int getPORT() {
        return PORT;
    }

}
