package client;

import dto.ChampionsIndexDto;
import dto.ItemsIndexDto;
import dto.RuneTreeDto;
import dto.SummonerSpellsDto;

import java.net.URI;
import java.util.List;
import java.util.Map;

public interface DataDragonClient {
    List<String> versions();
    String latestVersion();
    List<String> languages();

    ChampionsIndexDto champions(String version, String locale);
    URI championSquare(String version, String championId);
    URI championLoading(String version, String championId);
    URI championSplash(String championid, int skinNumber);

    ItemsIndexDto items(String version, String locale);
    URI itemIcon(String version, String itemId);

    SummonerSpellsDto summonerSpells(String version, String locale);
    URI summonerSpellIcon(String version, String spellId);

    List<RuneTreeDto> runes(String version, String locale);
    URI runeIcon(String iconPath);

    URI profileIcon(String version, int iconId);
}
