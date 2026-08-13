package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionDto(
        String id,
        String key,
        String name,
        String title,
        ChampionImageDto image,
        List<ChampionSkinDto> skins,
        String lore,
        String blurb,
        List<String> allytips,
        List<String> enemytips,
        List<String> tags,
        String partype,
        ChampionInfoDto info,
        ChampionStatsDto stats,
        List<ChampionSpellDto> spells,
        ChampionPassiveDto passive,
        List<Object> recommended
) {
}
