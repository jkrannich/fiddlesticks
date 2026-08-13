package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionDetailsDto(
        String type,
        String format,
        String version,
        Map<String, ChampionDto> data
) {
}
