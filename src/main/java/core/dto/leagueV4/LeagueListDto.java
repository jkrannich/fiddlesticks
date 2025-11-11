package core.dto.leagueV4;

public record LeagueListDto(String leagueId, List<LeagueItemDto> entries, String tier, String name, String queue) {
}
