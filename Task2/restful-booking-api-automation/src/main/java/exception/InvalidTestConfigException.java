package main.java.exception;

public class InvalidTestConfigException extends RuntimeException {
    public InvalidTestConfigException(String message) {
        super(message);
    }
}
