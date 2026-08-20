package wrapper.domain;

/** Player-facing match data used to identify a teammate or opponent. */
public record MatchParticipant(
        String puuid,
        String summonerName,
        String championName,
        int championId,
        int teamId,
        String role,
        String teamPosition,
        int kills,
        int deaths,
        int assists,
        boolean win
) {}
