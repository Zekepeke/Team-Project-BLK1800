package src.Server.AuthenticationException.SignupExceptions;

public class UserExistsException extends SignupException{
    public UserExistsException(String message) {
        super(message);
    }
    public UserExistsException() {
        super("");
    }
}
