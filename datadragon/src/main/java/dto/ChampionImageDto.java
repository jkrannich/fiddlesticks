package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionImageDto(
        String full,
        String sprite,
        String group,
        int x,
        int y,
        int w,
        int h
) {
}
