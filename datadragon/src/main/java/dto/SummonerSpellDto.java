package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SummonerSpellDto(
        String id,
        String name,
        String description,
        String tooltip,
        String key,
        int summonerLevel,
        List<String> modes
) {
}
