package core.client;

import core.config.Regions;
import core.http.RiotHttp;

import java.net.URI;

public final class ClashV1Client {

    private final RiotHttp riotHttp;

    public ClashV1Client(RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public List<PlayerDto> getPlayersByPuuid(Regions.PlatformRegion platformRegion, String puuid) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/clash/v1/players/by-puuid/" + puuid);
        return riotHttp.get(uri, PlayerDto.class).body();
    }
}
