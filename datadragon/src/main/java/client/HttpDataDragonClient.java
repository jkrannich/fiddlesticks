package client;

import dto.ChampionsIndexDto;
import util.HttpUtils;

import java.net.URI;
import java.util.List;
import java.util.Map;

public final class HttpDataDragonClient implements DataDragonClient {

    private static final String BASE = "https://ddragon.leagueoflegends.com";

    @Override
    public List<String> versions() {
        return HttpUtils.readArray(
                BASE + "/api/versions.json",
                String[].class
        );
    }

    @Override
    public String latestVersion() {
        List<String> v = versions();
        return v.isEmpty() ? null : v.get(0);
    }

    @Override
    public List<String> languages() {
        return HttpUtils.readArray(
                BASE + "/cdn/languages.json",
                String[].class
        );
    }

    @Override
    public ChampionsIndexDto champions(String version, String locale) {
        return HttpUtils.read(
                BASE + "/cdn/" + version + "/data/" + locale + "/champion.json",
                ChampionsIndexDto.class
        );
    }

    @Override
    public URI championSquare(String version, String championId) {
        return URI.create(
                BASE + "/cdn/" + version + "/img/champion/" + championId + ".png"
        );
    }

    @Override
    public URI championLoading(String version, String championId) {
        return URI.create(
                BASE + "/cdn/img/champion/loading/" + championId + "_0.jpg"
        );
    }

    @Override
    public URI championSplash(String championid, int skinNumber) {
        return URI.create(
                BASE + "/cdn/img/champion/splash/" + championid + "_" + skinNumber + ".jpg"
        );
    }

    @Override
    public Map<String, Object> items(String version, String locale) {
        return HttpUtils.readMap(
                BASE + "/cdn/" + version + "/data/" + locale + "/item.json"
        );
    }

    @Override
    public URI itemIcon(String version, String itemId) {
        return URI.create(
                BASE + "/cdn/" + version + "/img/item/" + itemId + ".png"
        );
    }

    @Override
    public Map<String, Object> summonerSpells(String version, String locale) {
        return HttpUtils.readMap(
                BASE + "/cdn/" + version + "/data/" + locale + "/summoner.json"
        );
    }

    @Override
    public URI summonerSpellIcon(String version, String spellId) {
        return URI.create(
                BASE + "/cdn/" + version + "/img/spell/" + spellId + ".png"
        );
    }

    @Override
    public Map<String, Object> runes(String version, String locale) {
        return HttpUtils.readMap(
                BASE + "/cdn/" + version + "/data/" + locale + "/runesReforged.json"
        );
    }

    @Override
    public URI runeIcon(String iconPath) {
        return URI.create(BASE + "/cdn/img/" + iconPath);
    }

    @Override
    public URI profileIcon(String version, int iconId) {
        return URI.create(
                BASE + "/cdn/" + version + "/img/profileicon" + iconId + ".png"
        );
    }
}
