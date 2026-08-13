package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionSpellDto(
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
        String resource
) {
}
