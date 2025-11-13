package core.dto.challenges;

import core.enums.State;
import core.enums.Tracking;

import java.util.Map;

public record ChallengeConfigInfoDto(
        long id,
        Map<String, Map<String, String>> localizedNames,
        State state,
        Tracking tracking,
        long startTimestamp,
        long endTimestamp,
        boolean leaderboard,
        Map<String, Double> threshholds) {}
