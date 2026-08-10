package wrapper.service;

import core.RiotApi;
import core.config.Regions;
import core.dto.account.AccountDto;
import core.dto.matchV5.MatchDto;
import core.model.MatchHistoryQuery;
import core.model.Puuid;
import core.model.RiotId;
import wrapper.domain.MatchSummary;
import wrapper.domain.SummonerProfile;
import wrapper.mapping.MatchMapper;
import wrapper.mapping.SummonerMapper;

import java.util.ArrayList;
import java.util.List;

/** Small convenience layer for common player-facing operations. */
public final class RiotLeagueService {
    private final RiotApi riotApi;

    public RiotLeagueService(final RiotApi riotApi) {
        this.riotApi = riotApi;
    }

    public String getPuuid(final Regions.RegionalRoute route, final String name, final String tag) {
        return getPuuid(route, RiotId.of(name, tag)).value();
    }

    public Puuid getPuuid(final Regions.RegionalRoute route, final RiotId riotId) {
        return Puuid.of(riotApi.regional(route).accounts().byRiotId(riotId).puuid());
    }

    public List<MatchSummary> getMatchHistory(
            final Regions.RegionalRoute route,
            final String name,
            final String tag,
            final int limit
    ) {
        return getMatchHistory(route, RiotId.of(name, tag), MatchHistoryQuery.firstPage(limit));
    }

    public List<MatchSummary> getMatchHistory(
            final Regions.RegionalRoute route,
            final RiotId riotId,
            final MatchHistoryQuery query
    ) {
        final Puuid puuid = getPuuid(route, riotId);
        final var matches = riotApi.regional(route).matches();
        final List<String> matchIds = matches.getMatchIdsByPuuid(puuid, query);

        final List<MatchSummary> summaries = new ArrayList<>(matchIds.size());
        for (final String matchId : matchIds) {
            final MatchDto match = matches.getMatchByMatchId(matchId);
            summaries.add(MatchMapper.toSummaryForPuuid(match, puuid.value()));
        }
        return List.copyOf(summaries);
    }

    public SummonerProfile getSummonerProfile(
            final Regions.RegionalRoute route,
            final Regions.PlatformRegion region,
            final String name,
            final String tag
    ) {
        final AccountDto account = riotApi.regional(route).accounts().byRiotId(name, tag);
        final var summoner = riotApi.platform(region).summoners().byPuuid(account.puuid());
        return SummonerMapper.toProfile(account.gameName(), account.tagLine(), summoner);
    }
}
