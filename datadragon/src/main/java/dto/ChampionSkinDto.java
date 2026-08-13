package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionSkinDto(
        String id,
        int num,
        String name,
        boolean chromas,
        Integer parentSkin
) {
}
