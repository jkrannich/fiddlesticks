package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SummonerSpellDto(
        String id,
        String name,
        String description,
        String tooltip,
        @JsonProperty("leveltip") ChampionLevelTipDto levelTip,
        @JsonProperty("maxrank") int maxRank,
        List<Double> cooldown,
        String cooldownBurn,
        List<Double> cost,
        String costBurn,
        @JsonProperty("datavalues") Map<String, Object> dataValues,
        List<List<Double>> effect,
        List<String> effectBurn,
        List<Map<String, Object>> vars,
        String costType,
        @JsonProperty("maxammo") String maxAmmo,
        List<Double> range,
        String rangeBurn,
        ChampionImageDto image,
        String resource,
        String key,
        List<String> modes,
        int summonerLevel
) {

    /** Compatibility constructor for the original summoner spell DTO shape. */
    public SummonerSpellDto(
            final String id,
            final String name,
            final String description,
            final String tooltip,
            final String key,
            final int summonerLevel,
            final List<String> modes
    ) {
        this(
                id,
                name,
                description,
                tooltip,
                null,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                key,
                modes,
                summonerLevel
        );
    }
}
