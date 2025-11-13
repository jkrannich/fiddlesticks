package core.client;

import core.config.Regions;
import core.dto.match.MatchDto;
import core.http.RiotHttp;

import java.net.URI;

public final class MatchV5Client {
    private final RiotHttp riotHttp;

    public MatchV5Client(final RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public String[] idsByPuuid(final Regions.RegionalRoute route, final String puuid, final int start, final int count) {
        final String url = route.baseUrl() + "/lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=" + start + "&count=" + count;
        final URI uri = URI.create(url);
        return riotHttp.get(uri, String[].class).body();
    }

    public MatchDto match(final Regions.RegionalRoute route, final String matchId) {
        final URI uri = URI.create(route.baseUrl() + "/lol/match/v5/matches/" + matchId);
        return riotHttp.get(uri, MatchDto.class).body();
    }
}
