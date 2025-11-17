package core.client;

import core.config.Regions;
import core.dto.spectator.CurrentGameInfo;
import core.http.RiotHttp;

import java.net.URI;

public final class SpectatorV5Client {
    private final RiotHttp riotHttp;

    public SpectatorV5Client(final RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public CurrentGameInfo getCurrentGameInfoForGivenPuuid(final Regions.PlatformRegion platformRegion, final String puuid) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/spectator/v5/active-games/by-summoner/" + puuid);
        return riotHttp.get(uri, CurrentGameInfo.class).body();
    }
}
