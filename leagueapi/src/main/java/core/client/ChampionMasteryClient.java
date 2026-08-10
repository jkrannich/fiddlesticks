package core.client;

import core.config.Regions;
import core.dto.championMastery.ChampionMasteryDto;
import core.http.RiotHttp;
import core.http.RiotUriBuilder;
import core.model.Puuid;

import java.net.URI;
import java.util.List;

/** Thin Champion-Mastery-V4 endpoint client. */
public final class ChampionMasteryClient {
    private final RiotHttp riotHttp;
    private final Regions.PlatformRegion defaultRegion;

    public ChampionMasteryClient(final RiotHttp riotHttp) {
        this(riotHttp, null);
    }

    public ChampionMasteryClient(final RiotHttp riotHttp, final Regions.PlatformRegion defaultRegion) {
        this.riotHttp = riotHttp;
        this.defaultRegion = defaultRegion;
    }

    public List<ChampionMasteryDto> getChampionMasteriesByPuuid(final Puuid puuid) {
        return getChampionMasteriesByPuuid(requireDefaultRegion(), puuid.value());
    }

    public List<ChampionMasteryDto> getChampionMasteriesByPuuid(final Regions.PlatformRegion server, final String puuid) {
        final URI uri = RiotUriBuilder.path(
                server.baseUrl(), "lol", "champion-mastery", "v4", "champion-masteries", "by-puuid", puuid
        );
        return List.of(riotHttp.get(uri, ChampionMasteryDto[].class).body());
    }

    public ChampionMasteryDto getChampionMasteriesByPuuidAndChampionId(final int championId, final String puuid) {
        return getChampionMasteriesByPuuidAndChampionId(requireDefaultRegion(), puuid, championId);
    }

    public ChampionMasteryDto getChampionMasteriesByPuuidAndChampionId(
            final Regions.PlatformRegion server,
            final String puuid,
            final int championId
    ) {
        final URI uri = RiotUriBuilder.path(
                server.baseUrl(),
                "lol", "champion-mastery", "v4", "champion-masteries", "by-puuid", puuid,
                "by-champion", Integer.toString(championId)
        );
        return riotHttp.get(uri, ChampionMasteryDto.class).body();
    }

    public List<ChampionMasteryDto> getChampionMasteriesByPuuidTop(final String puuid) {
        return getChampionMasteriesByPuuidTop(requireDefaultRegion(), puuid);
    }

    public List<ChampionMasteryDto> getChampionMasteriesByPuuidTop(
            final Regions.PlatformRegion server,
            final String puuid
    ) {
        final URI uri = RiotUriBuilder.path(
                server.baseUrl(), "lol", "champion-mastery", "v4", "champion-masteries", "by-puuid", puuid, "top"
        );
        return List.of(riotHttp.get(uri, ChampionMasteryDto[].class).body());
    }

    public int getTotalMasteryScore(final String puuid) {
        return getTotalMasteryScore(requireDefaultRegion(), puuid);
    }

    public int getTotalMasteryScore(final Regions.PlatformRegion server, final String puuid) {
        final URI uri = RiotUriBuilder.path(
                server.baseUrl(), "lol", "champion-mastery", "v4", "scores", "by-puuid", puuid
        );
        return riotHttp.get(uri, Integer.class).body();
    }

    private Regions.PlatformRegion requireDefaultRegion() {
        if (defaultRegion == null) {
            throw new IllegalStateException("No default platform region configured; use the region overload or RiotApi.builder()");
        }
        return defaultRegion;
    }
}
