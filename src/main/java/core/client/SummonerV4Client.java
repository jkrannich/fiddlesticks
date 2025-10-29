package core.client;

import core.config.Regions;
import core.dto.summoner.SummonerDto;
import core.http.RiotHttp;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class SummonerV4Client {
    private final RiotHttp riotHttp;

    public SummonerV4Client(RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public SummonerDto byName(String summonerName, Regions.PlatformRegion platformRegion) {
        String encoded = URLEncoder.encode(summonerName, StandardCharsets.UTF_8);
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/summoner/v4/summoners/by-name/" + encoded);
        return riotHttp.get(uri, SummonerDto.class).body();
    }
}
