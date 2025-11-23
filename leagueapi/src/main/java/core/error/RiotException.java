package core.error;

public class RiotException extends RuntimeException {
    public RiotException(String message) {
        super(message);
    }

    public RiotException(String message, Throwable cause) {
        super(message, cause);
    }
}
