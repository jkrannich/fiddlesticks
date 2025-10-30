package core.error;

public final class RiotNotFoundException extends RiotException {
    public RiotNotFoundException(String path) {
        super("Not found: " + path);
    }
}
