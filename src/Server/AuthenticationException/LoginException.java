package src.Server.AuthenticationException;

public class LoginException extends AuthenticationException {

    public LoginException(String message){
        super(message);
    }
    public LoginException() {
        super("");
    }
}
