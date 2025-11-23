package core.dto.challenges;

import java.util.List;
import java.util.Map;

public record PlayerInfoDto(
        List<ChallengeInfoDto> challenges,
        PlayerClientPreferencesDto preferences,
        ChallengePointDto totalPoints,
        Map<String, ChallengePointDto> categoryPoints) {
}
