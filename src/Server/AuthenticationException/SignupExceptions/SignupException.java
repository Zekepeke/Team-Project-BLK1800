package src.Server.AuthenticationException.SignupExceptions;

import src.Server.AuthenticationException.AuthenticationException;

public class SignupException extends AuthenticationException {
    public SignupException(String message) {
        super(message);
    }
    public SignupException() {
        super("");
    }
}
