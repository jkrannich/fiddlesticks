package core.error;

public final class RiotNotFoundException extends RuntimeException {
    public RiotNotFoundException(String path) {
        super("Not found: " + path);
    }
}
