package Exceptions;

public class DisconnectException extends RuntimeException{
    public DisconnectException() {
        super();
    }
    public DisconnectException(String message) {
        super(message);
    }
}
