package core.client;

import core.config.Regions;
import core.dto.leagueExp.LeagueEntryDto;
import core.dto.leagueV4.LeagueListDto;
import core.enums.Division;
import core.enums.Queue;
import core.enums.Tier;
import core.http.RiotHttp;
import core.http.RiotUriBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;

/** Thin League-V4 endpoint client. */
public final class LeagueClient {
    private final RiotHttp riotHttp;
    private final Regions.PlatformRegion defaultRegion;

    public LeagueClient(final RiotHttp riotHttp) {
        this(riotHttp, null);
    }

    public LeagueClient(final RiotHttp riotHttp, final Regions.PlatformRegion defaultRegion) {
        this.riotHttp = riotHttp;
        this.defaultRegion = defaultRegion;
    }

    public LeagueListDto getChallengerLeagueForGivenQueue(final Queue queueType) {
        return getChallengerLeagueForGivenQueue(requireDefaultRegion(), queueType);
    }

    public LeagueListDto getChallengerLeagueForGivenQueue(
            final Regions.PlatformRegion platformRegion,
            final Queue queueType
    ) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(), "lol", "league", "v4", "challengerleagues", "by-queue", queueType.name()
        );
        return riotHttp.get(uri, LeagueListDto.class).body();
    }

    public Set<LeagueEntryDto> getLeagueEntriesInAllQueuesByPuuid(final String puuid) {
        return getLeagueEntriesInAllQueuesByPuuid(requireDefaultRegion(), puuid);
    }

    public Set<LeagueEntryDto> getLeagueEntriesInAllQueuesByPuuid(
            final Regions.PlatformRegion platformRegion,
            final String puuid
    ) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(), "lol", "league", "v4", "entries", "by-puuid", puuid
        );
        return Set.of(riotHttp.get(uri, LeagueEntryDto[].class).body());
    }

    /** Ordered collection variant for callers that want to preserve Riot's response order. */
    public List<LeagueEntryDto> entriesByPuuid(final String puuid) {
        final Regions.PlatformRegion region = requireDefaultRegion();
        final URI uri = RiotUriBuilder.path(
                region.baseUrl(), "lol", "league", "v4", "entries", "by-puuid", puuid
        );
        return List.of(riotHttp.get(uri, LeagueEntryDto[].class).body());
    }

    public Set<LeagueEntryDto> getAllLeagueEntries(
            final Queue queueType,
            final Tier tier,
            final Division division,
            final int page
    ) {
        return getAllLeagueEntries(requireDefaultRegion(), queueType, tier, division, page);
    }

    public Set<LeagueEntryDto> getAllLeagueEntries(
            final Regions.PlatformRegion platformRegion,
            final Queue queueType,
            final Tier tier,
            final Division division,
            final int page
    ) {
        final URI uri = RiotUriBuilder.pathAndQuery(
                platformRegion.baseUrl(),
                List.of("lol", "league", "v4", "entries", queueType.name(), tier.name(), division.name()),
                java.util.Map.of("page", Integer.toString(page))
        );
        return Set.of(riotHttp.get(uri, LeagueEntryDto[].class).body());
    }

    public LeagueListDto getGrandMasterLeagueForGivenQueue(final Queue queueType) {
        return getGrandMasterLeagueForGivenQueue(requireDefaultRegion(), queueType);
    }

    public LeagueListDto getGrandMasterLeagueForGivenQueue(
            final Regions.PlatformRegion platformRegion,
            final Queue queueType
    ) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(), "lol", "league", "v4", "grandmasterleagues", "by-queue", queueType.name()
        );
        return riotHttp.get(uri, LeagueListDto.class).body();
    }

    public LeagueListDto getLeagueWithGivenIdIncludingInactiveEntries(final String leagueId) {
        return getLeagueWithGivenIdIncludingInactiveEntries(requireDefaultRegion(), leagueId);
    }

    public LeagueListDto getLeagueWithGivenIdIncludingInactiveEntries(
            final Regions.PlatformRegion platformRegion,
            final String leagueId
    ) {
        final URI uri = RiotUriBuilder.path(platformRegion.baseUrl(), "lol", "league", "v4", "leagues", leagueId);
        return riotHttp.get(uri, LeagueListDto.class).body();
    }

    public LeagueListDto getMasterLeagueForGivenQueue(final Queue queueType) {
        return getMasterLeagueForGivenQueue(requireDefaultRegion(), queueType);
    }

    public LeagueListDto getMasterLeagueForGivenQueue(
            final Regions.PlatformRegion platformRegion,
            final Queue queueType
    ) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(), "lol", "league", "v4", "masterleagues", "by-queue", queueType.name()
        );
        return riotHttp.get(uri, LeagueListDto.class).body();
    }

    private Regions.PlatformRegion requireDefaultRegion() {
        if (defaultRegion == null) {
            throw new IllegalStateException("No default platform region configured; use the region overload or RiotApi.builder()");
        }
        return defaultRegion;
    }
}
