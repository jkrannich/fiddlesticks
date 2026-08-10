package core.client;

import core.config.Regions;
import core.dto.spectator.CurrentGameInfo;
import core.http.RiotHttp;
import core.http.RiotUriBuilder;

import java.net.URI;

/** Thin Spectator-V5 endpoint client. */
public final class SpectatorClient {
    private final RiotHttp riotHttp;
    private final Regions.PlatformRegion defaultRegion;

    public SpectatorClient(final RiotHttp riotHttp) {
        this(riotHttp, null);
    }

    public SpectatorClient(final RiotHttp riotHttp, final Regions.PlatformRegion defaultRegion) {
        this.riotHttp = riotHttp;
        this.defaultRegion = defaultRegion;
    }

    public CurrentGameInfo getCurrentGameInfoForGivenPuuid(final String puuid) {
        return getCurrentGameInfoForGivenPuuid(requireDefaultRegion(), puuid);
    }

    public CurrentGameInfo getCurrentGameInfoForGivenPuuid(
            final Regions.PlatformRegion platformRegion,
            final String puuid
    ) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(), "lol", "spectator", "v5", "active-games", "by-summoner", puuid
        );
        return riotHttp.get(uri, CurrentGameInfo.class).body();
    }

    private Regions.PlatformRegion requireDefaultRegion() {
        if (defaultRegion == null) {
            throw new IllegalStateException("No default platform region configured; use the region overload or RiotApi.builder()");
        }
        return defaultRegion;
    }
}
