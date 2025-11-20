import java.net.URI;
import java.util.List;

public interface DataDragonClient {
    List<String> versions();
    String latestVersion();
    List<String> languages();

    URI championSquare(String version, String championId);
    URI itemIcon(String version, int itemId);
    URI summonerSpellIcon(String version, String spellId);
    URI profileIcon(String version, int iconId);
}
