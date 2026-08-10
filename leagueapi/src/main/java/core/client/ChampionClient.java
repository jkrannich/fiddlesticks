package core.client;

import core.config.Regions;
import core.dto.championRotation.ChampionInfoDto;
import core.http.RiotHttp;
import core.http.RiotUriBuilder;

import java.net.URI;

/** Thin Champion-V3 rotation endpoint client. */
public final class ChampionClient {
    private final RiotHttp riotHttp;
    private final Regions.PlatformRegion defaultRegion;

    public ChampionClient(final RiotHttp riotHttp) {
        this(riotHttp, null);
    }

    public ChampionClient(final RiotHttp riotHttp, final Regions.PlatformRegion defaultRegion) {
        this.riotHttp = riotHttp;
        this.defaultRegion = defaultRegion;
    }

    public ChampionInfoDto getChampionRotation() {
        return getChampionRotation(requireDefaultRegion());
    }

    public ChampionInfoDto getChampionRotation(final Regions.PlatformRegion platform) {
        final URI uri = RiotUriBuilder.path(platform.baseUrl(), "lol", "platform", "v3", "champion-rotations");
        return riotHttp.get(uri, ChampionInfoDto.class).body();
    }

    private Regions.PlatformRegion requireDefaultRegion() {
        if (defaultRegion == null) {
            throw new IllegalStateException("No default platform region configured; use the region overload or RiotApi.builder()");
        }
        return defaultRegion;
    }
}
