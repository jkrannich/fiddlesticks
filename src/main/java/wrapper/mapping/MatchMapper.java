package wrapper.mapping;

import core.dto.matchV5.MatchDto;
import core.dto.matchV5.Participant;
import wrapper.domain.MatchSummary;

public final class MatchMapper {
    private MatchMapper() {}

    public static MatchSummary toSummaryForPuuid(MatchDto dto, String puuid) {
        Participant p = dto.info().participants().stream()
                .filter(participant -> puuid.equals(participant.puuid()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No participant found for puuid: " + puuid));

        return new MatchSummary(
                dto.metadata().matchId(),
                dto.info().gameMode(),
                dto.info().gameDuration(),
                p.championName(),
                p.kills(),
                p.deaths(),
                p.assists(),
                p.win()
        );
    }
}
