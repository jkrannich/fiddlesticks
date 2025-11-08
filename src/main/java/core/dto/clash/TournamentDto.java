package core.dto.clash;

public record TournamentDto(int id, int themeId, String nameKey, String nameKeySecondary, List<TournamentPhaseDto> schedule) {
}
