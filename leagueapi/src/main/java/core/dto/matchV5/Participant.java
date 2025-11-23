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
        Perks perks
) {}
