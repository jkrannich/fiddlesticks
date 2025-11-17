package core.dto.spectator;

import core.dto.matchV5.Perks;

public record CurrentGameParticipant(
        long championId,
        Perks perks,
        long profileIconId,
        boolean bot,
        long teamId,
        String puuid,
        long spell1Id,
        long spell2Id,
        List<GameCustomizationObject> gameCustomizationObjects
) {
}
