package src.server.AuthenticationException.SignupExceptions;

import src.server.AuthenticationException.AuthenticationException;

public class SignupException extends AuthenticationException {
    public SignupException(String message) {
        super(message);
    }
    public SignupException() {
        super("");
    }
}
