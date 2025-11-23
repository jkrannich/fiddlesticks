package wrapper.service;

import core.RiotApi;
import core.config.Regions;
import wrapper.domain.MatchSummary;
import wrapper.domain.SummonerProfile;
import wrapper.mapping.MatchMapper;
import wrapper.mapping.SummonerMapper;

import java.util.ArrayList;
import java.util.List;

public final class RiotLeagueService {

    private final RiotApi riotApi;

    public RiotLeagueService(final RiotApi riotApi) {
        this.riotApi = riotApi;
    }

    public String getPuuid(final Regions.RegionalRoute route, final String name, final String tag) {
        return riotApi.account().byRiotId(route, name, tag).puuid();
    }

    public List<MatchSummary> getMatchHistory(final Regions.RegionalRoute route, final String name, final String tag, int limit) {
        final String puuid = getPuuid(route, name, tag);

        final String[] matchIds = riotApi.match().getListOfMatchIdsByPuuid(route, puuid, 0, limit);

        final List<MatchSummary> summaries = new ArrayList<>();

        for (String matchId : matchIds) {
            var dto = riotApi.match().getMatchByMatchId(route, matchId);
            var summary = MatchMapper.toSummaryForPuuid(dto, puuid);
            summaries.add(summary);
        }
        return summaries;
    }

    public SummonerProfile getSummonerProfile(final Regions.RegionalRoute route, final Regions.PlatformRegion region, final String name, final String tag) {
        var account = riotApi.account().byRiotId(route, name, tag);

        var summoner = riotApi.summoner().byPuuid(region, account.puuid());

        return SummonerMapper.toProfile(account.gameName(), account.tagLine(), summoner);
    }
}
