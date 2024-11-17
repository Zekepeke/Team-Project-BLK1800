package src.server.AuthenticationException;
import src.SocketIO;

public class UserChatActiveException extends AuthenticationException{
    private final String type = SocketIO.ERROR_USER_ALREADY_ACTIVE;
    public UserChatActiveException(String message) {
        super(message);
    }
    public UserChatActiveException() {
        super(SocketIO.ERROR_USER_ALREADY_ACTIVE);
    }
}
