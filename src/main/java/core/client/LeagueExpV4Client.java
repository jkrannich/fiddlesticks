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

    public LeagueExpV4Client(RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public Set<LeagueEntryDto> getAllLeagueEntries(Regions.PlatformRegion platformRegion,
                                                   int page,
                                                   Queue queueType,
                                                   Tier tier,
                                                   Division division) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/league-exp/v4/entries/" + queueType.name() + "/" + tier.name() + "/" + division.name() + "?page=" + page);
        return Set.of(riotHttp.get(uri, LeagueEntryDto.class).body());
    }
}
