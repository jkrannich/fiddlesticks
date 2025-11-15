package core.client;

import core.config.Regions;
import core.http.RiotHttp;

import java.net.URI;

public final class LolStatusV4Client {
    private RiotHttp riotHttp;

    public LolStatusV4Client(final RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public PlatformDataDto getStatusForGivenPlatform(Regions.PlatformRegion platformRegion) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/status/v4/platform-data");
        return riotHttp.get(uri, PlatformDataDto.class).body();
    }
}
