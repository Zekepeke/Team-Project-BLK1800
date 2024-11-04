package Exceptions;

public class InvalidUsernameException extends Exception {
    public InvalidUsernameException(String message) {
        super(message);
    }
    public InvalidUsernameException() {
        super("Invalid Username!");
    }
}
