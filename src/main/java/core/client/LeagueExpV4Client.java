package core.client;

import core.config.Regions;
import core.http.RiotHttp;

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

    }
}
