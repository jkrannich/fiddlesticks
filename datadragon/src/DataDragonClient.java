import java.util.List;

public interface DataDragonClient {
    List<String> versions();
    String latestVersion();
    List<String> languages();
}
