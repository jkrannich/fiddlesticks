package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionPassiveDto(
        String name,
        String description,
        ChampionImageDto image
) {
}
