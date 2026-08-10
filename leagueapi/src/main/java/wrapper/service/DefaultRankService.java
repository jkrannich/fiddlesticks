package wrapper.service;

import core.RiotApi;
import core.config.Regions;
import core.dto.account.AccountDto;
import core.dto.leagueExp.LeagueEntryDto;
import core.enums.Queue;
import wrapper.domain.RankSnapshot;
import wrapper.domain.RankedQueue;

import java.util.Optional;
import java.util.Set;

public final class DefaultRankService implements RankService {

    private final RiotApi riotApi;

    public DefaultRankService(final RiotApi riotApi) {
        this.riotApi = riotApi;
    }

    @Override
    public Optional<RankSnapshot> rankByRiotId(Regions.RegionalRoute route, Regions.PlatformRegion platformRegion, String gameName, String tagLine, RankedQueue queue) {
        AccountDto accountDto = riotApi.regional(route).accounts().byRiotId(gameName, tagLine);
        String puuid = accountDto.puuid();

        Set<LeagueEntryDto> entries = riotApi.platform(platformRegion).league().getLeagueEntriesInAllQueuesByPuuid(puuid);

        Queue targetQueue = switch (queue) {
            case SOLO_5X5 -> Queue.RANKED_SOLO_5x5;
            case FLEX_SR -> Queue.RANKED_FLEX_SR;
        };

        return entries.stream()
                .filter(e -> targetQueue.name().equals(e.queueType()))
                .findFirst()
                .map(e -> new RankSnapshot(
                        queue,
                        e.tier(),
                        e.rank(),
                        e.leaguePoints(),
                        e.wins(),
                        e.losses()
                ));
    }
}
