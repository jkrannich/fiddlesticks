package wrapper.domain;

public record MatchSummary(
        String matchId,
        String gameMode,
        long durationSeconds,
        String champion,
        int kills,
        int deaths,
        int assists,
        boolean win
) {}
