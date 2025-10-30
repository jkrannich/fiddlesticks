package core.client;

import core.config.Regions;
import core.dto.match.MatchDto;
import core.http.RiotHttp;

import java.net.URI;

public final class MatchV5Client {
    private final RiotHttp riotHttp;

    public MatchV5Client(RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public String[] idsByPuuid(Regions.RegionalRoute route, String puuid, int start, int count) {
        String url = route.baseUrl() + "/lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=" + start + "&count=" + count;
        URI uri = URI.create(url);
        return riotHttp.get(uri, String[].class).body();
    }

    public MatchDto match(Regions.RegionalRoute route, String matchId) {
        URI uri = URI.create(route.baseUrl() + "/lol/match/v5/matches/" + matchId);
        return riotHttp.get(uri, MatchDto.class).body();
    }
}
