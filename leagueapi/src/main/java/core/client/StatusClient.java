package core.client;

import core.config.Regions;
import core.dto.status.PlatformDataDto;
import core.http.RiotHttp;
import core.http.RiotUriBuilder;

import java.net.URI;

/** Thin LoL-Status-V4 endpoint client. */
public final class StatusClient {
    private final RiotHttp riotHttp;
    private final Regions.PlatformRegion defaultRegion;

    public StatusClient(final RiotHttp riotHttp) {
        this(riotHttp, null);
    }

    public StatusClient(final RiotHttp riotHttp, final Regions.PlatformRegion defaultRegion) {
        this.riotHttp = riotHttp;
        this.defaultRegion = defaultRegion;
    }

    public PlatformDataDto getStatusForGivenPlatform() {
        return getStatusForGivenPlatform(requireDefaultRegion());
    }

    public PlatformDataDto getStatusForGivenPlatform(final Regions.PlatformRegion platformRegion) {
        final URI uri = RiotUriBuilder.path(platformRegion.baseUrl(), "lol", "status", "v4", "platform-data");
        return riotHttp.get(uri, PlatformDataDto.class).body();
    }

    private Regions.PlatformRegion requireDefaultRegion() {
        if (defaultRegion == null) {
            throw new IllegalStateException("No default platform region configured; use the region overload or RiotApi.builder()");
        }
        return defaultRegion;
    }
}
