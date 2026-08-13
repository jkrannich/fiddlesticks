package core.client;

import tools.jackson.core.type.TypeReference;
import core.config.Regions;
import core.dto.matchV5.MatchDto;
import core.dto.matchV5.TimelineDto;
import core.http.RiotHttp;
import core.http.RiotUriBuilder;
import core.model.MatchHistoryQuery;
import core.model.Puuid;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Thin Match-V5 endpoint client with a typed query object for match history. */
public final class MatchClient {
    private final RiotHttp riotHttp;
    private final Regions.RegionalRoute defaultRoute;

    public MatchClient(final RiotHttp riotHttp) {
        this(riotHttp, null);
    }

    public MatchClient(final RiotHttp riotHttp, final Regions.RegionalRoute defaultRoute) {
        this.riotHttp = riotHttp;
        this.defaultRoute = defaultRoute;
    }

    public List<String> getMatchIdsByPuuid(final Puuid puuid, final MatchHistoryQuery query) {
        return getMatchIdsByPuuid(requireDefaultRoute(), puuid.value(), query);
    }

    public List<String> getMatchIdsByPuuid(
            final Regions.RegionalRoute route,
            final String puuid,
            final MatchHistoryQuery query
    ) {
        final URI uri = RiotUriBuilder.pathAndQuery(
                route.baseUrl(),
                List.of("lol", "match", "v5", "matches", "by-puuid", puuid, "ids"),
                query.queryParameters()
        );
        return riotHttp.get(uri, new TypeReference<List<String>>() { }).body();
    }

    /** Compatibility method for the original array-based API. */
    public String[] getListOfMatchIdsByPuuid(
            final Regions.RegionalRoute route,
            final String puuid,
            final int start,
            final int count
    ) {
        final Map<String, String> query = new LinkedHashMap<>();
        query.put("start", Integer.toString(start));
        query.put("count", Integer.toString(count));
        final URI uri = RiotUriBuilder.pathAndQuery(
                route.baseUrl(),
                List.of("lol", "match", "v5", "matches", "by-puuid", puuid, "ids"),
                query
        );
        return riotHttp.get(uri, String[].class).body();
    }

    public MatchDto getMatchByMatchId(final String matchId) {
        return getMatchByMatchId(requireDefaultRoute(), matchId);
    }

    public MatchDto getMatchByMatchId(final Regions.RegionalRoute route, final String matchId) {
        final URI uri = RiotUriBuilder.path(route.baseUrl(), "lol", "match", "v5", "matches", matchId);
        return riotHttp.get(uri, MatchDto.class).body();
    }

    public TimelineDto getMatchTimelineByMatchId(final String matchId) {
        return getMatchTimelineByMatchId(requireDefaultRoute(), matchId);
    }

    public TimelineDto getMatchTimelineByMatchId(final Regions.RegionalRoute route, final String matchId) {
        final URI uri = RiotUriBuilder.path(
                route.baseUrl(),
                "lol", "match", "v5", "matches", matchId, "timeline"
        );
        return riotHttp.get(uri, TimelineDto.class).body();
    }

    private Regions.RegionalRoute requireDefaultRoute() {
        if (defaultRoute == null) {
            throw new IllegalStateException("No default regional route configured; use the route overload or RiotApi.builder()");
        }
        return defaultRoute;
    }
}
