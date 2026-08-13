package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionInfoDto(
        int attack,
        int defense,
        int magic,
        int difficulty
) {
}
