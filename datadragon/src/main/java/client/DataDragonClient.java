package client;

import dto.ChampionsIndexDto;

import java.net.URI;
import java.util.List;

public interface DataDragonClient {
    List<String> versions();
    String latestVersion();
    List<String> languages();

    ChampionsIndexDto champions(String version, String locale);

    URI championSquare(String version, String championId);
    URI profileIcon(String version, int iconId);
}
