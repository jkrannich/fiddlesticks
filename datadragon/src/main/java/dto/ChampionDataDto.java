package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionDataDto(
        String id,
        String key,
        String name,
        String title,
        ChampionImageDto image,
        String blurb,
        ChampionInfoDto info,
        List<String> tags,
        String partype,
        ChampionStatsDto stats
) {
}
