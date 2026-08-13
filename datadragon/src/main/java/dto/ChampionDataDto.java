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

    /** Compatibility constructor for the original summary-only DTO shape. */
    public ChampionDataDto(
            final String id,
            final String key,
            final String name,
            final String title,
            final List<String> tags,
            final ChampionStatsDto stats
    ) {
        this(id, key, name, title, null, null, null, tags, null, stats);
    }
}
