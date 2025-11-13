package core.client;

import core.config.Regions;
import core.dto.summoner.SummonerDto;
import core.http.RiotHttp;

import java.net.URI;

public final class SummonerV4Client {
    private final RiotHttp riotHttp;

    public SummonerV4Client(final RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public SummonerDto byPuuid(final Regions.PlatformRegion platformRegion, final String puuid) {
        final URI uri = URI.create(platformRegion.baseUrl() + "/lol/summoner/v4/summoners/by-puuid/" + puuid);
        return riotHttp.get(uri, SummonerDto.class).body();
    }
}
