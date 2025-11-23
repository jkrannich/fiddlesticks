package core.dto.leagueV4;

import java.util.List;

public record LeagueListDto(String leagueId, List<LeagueItemDto> entries, String tier, String name, String queue) {
}
