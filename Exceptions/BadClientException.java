package Exceptions;

public class BadClientException extends RuntimeException {
    public BadClientException(String message) {
        super(message);
    }
}
