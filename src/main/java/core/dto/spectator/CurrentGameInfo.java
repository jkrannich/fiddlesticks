package core.dto.spectator;

public record CurrentGameInfo(
        long gameId,
        String gameType,
        long gameStartTime,
        long mapId,
        long gameLength,
        String platformId,
        String gameMode,
        List<BannedChampions> bannedChampions,
        long gameQueueConfigId,
        Observer observers,
        List<CurrentGameParticipant> participants
) {
}
