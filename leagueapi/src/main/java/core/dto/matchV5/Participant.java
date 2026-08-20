package core.dto.matchV5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Participant(
        String puuid,
        String summonerName,
        String championName,
        int champLevel,
        int kills,
        int deaths,
        int assists,
        boolean win,
        int totalMinionsKilled,
        int neutralMinionsKilled,
        int wardsPlaced,
        int visionScore,
        int goldEarned,
        int totalDamageDealtToChampions,
        int totalDamageTaken,
        Perks perks,
        int teamId,
        String role,
        String teamPosition,
        int championId
) {
    /**
     * Keeps source compatibility with match participants created before the
     * team and position fields were exposed.
     */
    public Participant(
            final String puuid,
            final String summonerName,
            final String championName,
            final int champLevel,
            final int kills,
            final int deaths,
            final int assists,
            final boolean win,
            final int totalMinionsKilled,
            final int neutralMinionsKilled,
            final int wardsPlaced,
            final int visionScore,
            final int goldEarned,
            final int totalDamageDealtToChampions,
            final int totalDamageTaken,
            final Perks perks
    ) {
        this(
                puuid,
                summonerName,
                championName,
                champLevel,
                kills,
                deaths,
                assists,
                win,
                totalMinionsKilled,
                neutralMinionsKilled,
                wardsPlaced,
                visionScore,
                goldEarned,
                totalDamageDealtToChampions,
                totalDamageTaken,
                perks,
                0,
                null,
                null,
                0
        );
    }
}
