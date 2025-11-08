package core.dto.clash;

import core.enums.Position;
import core.enums.Role;

public record PlayerDto(String puuid, String teamId, Position position, Role role) {}
