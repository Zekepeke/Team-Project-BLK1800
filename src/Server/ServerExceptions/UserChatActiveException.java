<<<<<<< HEAD:src/Server/AuthenticationException/UserChatActiveException.java
package src.server.AuthenticationException;
=======
package src.Server.ServerExceptions;
>>>>>>> ServerBranch:src/Server/ServerExceptions/UserChatActiveException.java
import src.SocketIO;

public class UserChatActiveException extends Exception {
    private final String type = SocketIO.ERROR_USER_ALREADY_ACTIVE;
    public UserChatActiveException(String message) {
        super(message);
    }
    public UserChatActiveException() {
        super(SocketIO.ERROR_USER_ALREADY_ACTIVE);
    }
}
