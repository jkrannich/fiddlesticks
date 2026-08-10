package core.error;

public final class RiotServerException extends RiotException {
    public RiotServerException(int status, String path) {
        super("Server error: " + status + " at " + path);
    }
}
