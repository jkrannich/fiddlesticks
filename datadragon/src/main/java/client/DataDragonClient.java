package client;

import dto.ChampionsIndexDto;
import dto.ChampionDetailsDto;
import dto.ChampionDto;
import dto.ItemsIndexDto;
import dto.RealmDto;
import dto.RuneTreeDto;
import dto.SummonerSpellsDto;

import java.net.URI;
import java.util.List;

public interface DataDragonClient {
    List<String> versions();
    String latestVersion();
    List<String> languages();
    RealmDto realm(String platform);
    URI patchArchive(String version);
    byte[] downloadPatchArchive(String version);

    ChampionsIndexDto champions(String version, String locale);
    ChampionDetailsDto championDetails(String version, String locale, String championId);
    ChampionDto champion(String version, String locale, String championId);
    URI championSquare(String version, String championId);
    URI championLoading(String version, String championId);
    URI championSplash(String championid, int skinNumber);
    URI championPassive(String version, String imageFile);
    URI championSpell(String version, String imageFile);
    URI sprite(String version, String spriteFile);
    URI versionedAsset(String version, String directory, String fileName);

    ItemsIndexDto items(String version, String locale);
    URI itemIcon(String version, String itemId);

    SummonerSpellsDto summonerSpells(String version, String locale);
    URI summonerSpellIcon(String version, String spellId);

    List<RuneTreeDto> runes(String version, String locale);
    URI runeIcon(String iconPath);

    URI profileIcon(String version, int iconId);
}
