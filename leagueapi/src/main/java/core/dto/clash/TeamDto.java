package core.dto.clash;

import java.util.List;

public record TeamDto(String id, int tournamentId, String name, int iconId, int tier, String captain, String abbreviation, List<PlayerDto> players) {
}
