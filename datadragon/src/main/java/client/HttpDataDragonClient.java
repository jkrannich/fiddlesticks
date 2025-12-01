package client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ChampionsIndexDto;
import util.HttpUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

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
    public URI profileIcon(String version, int iconId) {
        return URI.create(
                BASE + "/cdn/" + version + "/img/profileicon" + iconId + ".png"
        );
    }
}
