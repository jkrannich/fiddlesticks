package core.dto.challenges;

import core.enums.Level;

public record ChallengeInfoDto(
        double percentile,
        int playersInLevel,
        long achievedTime,
        double value,
        long challengeId,
        Level level,
        int position
) {
}
