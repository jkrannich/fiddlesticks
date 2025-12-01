package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionsIndexDto(
        String type,
        String format,
        String version,
        Map<String, ChampionDataDto> data
) {
}
