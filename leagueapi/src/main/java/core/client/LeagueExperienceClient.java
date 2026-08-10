package core.client;

import core.config.Regions;
import core.dto.leagueExp.LeagueEntryDto;
import core.enums.Division;
import core.enums.Queue;
import core.enums.Tier;
import core.http.RiotHttp;
import core.http.RiotUriBuilder;

import java.net.URI;
import java.util.Set;

/** Thin League-Exp-V4 endpoint client. */
public final class LeagueExperienceClient {
    private final RiotHttp riotHttp;
    private final Regions.PlatformRegion defaultRegion;

    public LeagueExperienceClient(final RiotHttp riotHttp) {
        this(riotHttp, null);
    }

    public LeagueExperienceClient(final RiotHttp riotHttp, final Regions.PlatformRegion defaultRegion) {
        this.riotHttp = riotHttp;
        this.defaultRegion = defaultRegion;
    }

    public Set<LeagueEntryDto> getAllLeagueEntries(
            final int page,
            final Queue queueType,
            final Tier tier,
            final Division division
    ) {
        return getAllLeagueEntries(requireDefaultRegion(), page, queueType, tier, division);
    }

    public Set<LeagueEntryDto> getAllLeagueEntries(
            final Regions.PlatformRegion platformRegion,
            final int page,
            final Queue queueType,
            final Tier tier,
            final Division division
    ) {
        final URI uri = RiotUriBuilder.pathAndQuery(
                platformRegion.baseUrl(),
                java.util.List.of("lol", "league-exp", "v4", "entries", queueType.name(), tier.name(), division.name()),
                java.util.Map.of("page", Integer.toString(page))
        );
        return Set.of(riotHttp.get(uri, LeagueEntryDto[].class).body());
    }

    private Regions.PlatformRegion requireDefaultRegion() {
        if (defaultRegion == null) {
            throw new IllegalStateException("No default platform region configured; use the region overload or RiotApi.builder()");
        }
        return defaultRegion;
    }
}
