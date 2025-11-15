package core.dto.matchV5;

public record ParticipantFrameDto(
        ChampionStatsDto championStats,
        int currentGold,
        DamageStatsDto damageStats,
        int goldPerSecond,
        int jungleMinionsKilled,
        int level,
        int minionsKilled,
        int participantId,
        PositionDto position,
        int timeEnemySpentControlled,
        int totalGold,
        int xp
) {
}
