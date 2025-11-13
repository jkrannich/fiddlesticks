package core.client;

import core.config.Regions;
import core.dto.leagueExp.LeagueEntryDto;
import core.enums.Division;
import core.enums.Queue;
import core.enums.Tier;
import core.http.RiotHttp;

import java.net.URI;
import java.util.Set;

public final class LeagueExpV4Client {
    private final RiotHttp riotHttp;

    public LeagueExpV4Client(final RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public Set<LeagueEntryDto> getAllLeagueEntries(final Regions.PlatformRegion platformRegion,
                                                   final int page,
                                                   final Queue queueType,
                                                   final Tier tier,
                                                   final Division division) {
        final URI uri = URI.create(platformRegion.baseUrl() + "/lol/league-exp/v4/entries/" + queueType.name() + "/" + tier.name() + "/" + division.name() + "?page=" + page);
        return Set.of(riotHttp.get(uri, LeagueEntryDto[].class).body());
    }
}
