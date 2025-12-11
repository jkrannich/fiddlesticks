package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SummonerSpellsDto(
        String type,
        String version,
        Map<String, SummonerSpellDto> data
) {
}
