package wrapper.mapping;

import core.dto.matchV5.MatchDto;
import core.dto.matchV5.Participant;
import wrapper.domain.MatchParticipant;
import wrapper.domain.MatchSummary;

import java.util.List;

public final class MatchMapper {
    private MatchMapper() {}

    public static MatchSummary toSummaryForPuuid(MatchDto dto, String puuid) {
        Participant p = dto.info().participants().stream()
                .filter(participant -> puuid.equals(participant.puuid()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No participant found for puuid: " + puuid));

        final int teamId = p.teamId();
        final List<MatchParticipant> teammates = teamId > 0
                ? dto.info().participants().stream()
                .filter(participant -> !puuid.equals(participant.puuid()))
                .filter(participant -> participant.teamId() == teamId)
                .map(MatchMapper::toMatchParticipant)
                .toList()
                : List.of();
        final List<MatchParticipant> opponents = teamId > 0
                ? dto.info().participants().stream()
                .filter(participant -> participant.teamId() > 0)
                .filter(participant -> participant.teamId() != teamId)
                .map(MatchMapper::toMatchParticipant)
                .toList()
                : List.of();

        return new MatchSummary(
                dto.metadata().matchId(),
                dto.info().gameMode(),
                dto.info().gameDuration(),
                p.championName(),
                p.kills(),
                p.deaths(),
                p.assists(),
                p.win(),
                teammates,
                opponents
        );
    }

    private static MatchParticipant toMatchParticipant(final Participant participant) {
        return new MatchParticipant(
                participant.puuid(),
                participant.summonerName(),
                participant.championName(),
                participant.championId(),
                participant.teamId(),
                participant.role(),
                participant.teamPosition(),
                participant.kills(),
                participant.deaths(),
                participant.assists(),
                participant.win()
        );
    }
}
