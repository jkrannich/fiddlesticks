package core.dto.championRotation;

import java.util.List;

public record ChampionInfoDto(int maxNewPlayerLevel, List<Integer> freeChampionIdsForNewPlayers, List<Integer> freeChampionIds) {}
