package wrapper.domain;

import java.util.List;

public record MatchSummary(
        String matchId,
        String gameMode,
        long durationSeconds,
        String champion,
        int kills,
        int deaths,
        int assists,
        boolean win,
        List<MatchParticipant> teammates,
        List<MatchParticipant> opponents
) {
    /** Keeps the previous summary shape useful for callers that do not need participant details. */
    public MatchSummary(
            final String matchId,
            final String gameMode,
            final long durationSeconds,
            final String champion,
            final int kills,
            final int deaths,
            final int assists,
            final boolean win
    ) {
        this(matchId, gameMode, durationSeconds, champion, kills, deaths, assists, win, List.of(), List.of());
    }
}
