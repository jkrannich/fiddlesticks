package core.client;

import core.config.Regions;
import core.dto.leagueV4.LeagueListDto;
import core.enums.Queue;
import core.http.RiotHttp;

import java.net.URI;

public final class LeagueV4Client {
    private final RiotHttp riotHttp;

    public LeagueV4Client(RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public LeagueListDto getChallengerLeagueForGivenQueue(Regions.PlatformRegion platformRegion, Queue queueType) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/league/v4/challengerleagues/by-queue/" + queueType.name());
        return riotHttp.get(uri, LeagueListDto.class).body();
    }
}
