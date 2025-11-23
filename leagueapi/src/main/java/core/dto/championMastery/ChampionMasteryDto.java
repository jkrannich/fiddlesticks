package core.dto.championMastery;

public record ChampionMasteryDto(
        String puuid,
        long championPointsUntilNextLevel,
        boolean chestGranted,
        long championId,
        long lastTimePlayed,
        int championLevel,
        int championPoints
) {
}
