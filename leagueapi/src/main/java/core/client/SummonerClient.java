package core.client;

import core.config.Regions;
import core.dto.summoner.SummonerDto;
import core.http.RiotHttp;
import core.http.RiotUriBuilder;
import core.model.Puuid;

import java.net.URI;

/** Thin Summoner-V4 endpoint client. */
public final class SummonerClient {
    private final RiotHttp riotHttp;
    private final Regions.PlatformRegion defaultRegion;

    public SummonerClient(final RiotHttp riotHttp) {
        this(riotHttp, null);
    }

    public SummonerClient(final RiotHttp riotHttp, final Regions.PlatformRegion defaultRegion) {
        this.riotHttp = riotHttp;
        this.defaultRegion = defaultRegion;
    }

    public SummonerDto byPuuid(final Puuid puuid) {
        return byPuuid(requireDefaultRegion(), puuid);
    }

    public SummonerDto byPuuid(final String puuid) {
        return byPuuid(requireDefaultRegion(), Puuid.of(puuid));
    }

    public SummonerDto byPuuid(final Regions.PlatformRegion platformRegion, final String puuid) {
        return byPuuid(platformRegion, Puuid.of(puuid));
    }

    public SummonerDto byPuuid(final Regions.PlatformRegion platformRegion, final Puuid puuid) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(),
                "lol", "summoner", "v4", "summoners", "by-puuid", puuid.value()
        );
        return riotHttp.get(uri, SummonerDto.class).body();
    }

    public SummonerDto bySummonerId(final String summonerId) {
        return bySummonerId(requireDefaultRegion(), summonerId);
    }

    public SummonerDto bySummonerId(final Regions.PlatformRegion platformRegion, final String summonerId) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(),
                "lol", "summoner", "v4", "summoners", summonerId
        );
        return riotHttp.get(uri, SummonerDto.class).body();
    }

    private Regions.PlatformRegion requireDefaultRegion() {
        if (defaultRegion == null) {
            throw new IllegalStateException("No default platform region configured; use the region overload or RiotApi.builder()");
        }
        return defaultRegion;
    }
}
