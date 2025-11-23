package core.client;

import core.config.Regions;
import core.dto.championRotation.ChampionInfoDto;
import core.http.RiotHttp;

import java.net.URI;

public final class ChampionV3Client {
    private final RiotHttp riotHttp;

    public ChampionV3Client(final RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public ChampionInfoDto getChampionRotation(final Regions.PlatformRegion platform) {
        final URI uri = URI.create(platform.baseUrl() + "/lol/platform/v3/champion-rotations");
        return riotHttp.get(uri, ChampionInfoDto.class).body();
    }
}
