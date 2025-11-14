package core.dto.challenges;

public record ChallengePointDto(String level, long current, long max, long percentile) {
}
