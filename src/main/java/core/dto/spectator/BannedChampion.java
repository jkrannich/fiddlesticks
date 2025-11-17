package core.dto.spectator;

public record BannedChampion(
        int pickTurn,
        long championId,
        long teamId
) {
}
