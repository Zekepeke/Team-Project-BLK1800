package src.server.AuthenticationException;

public class LoginException extends AuthenticationException {

    public LoginException(String message){
        super(message);
    }
    public LoginException() {
        super("");
    }
}
