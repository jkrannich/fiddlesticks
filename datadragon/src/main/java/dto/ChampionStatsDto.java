package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionStatsDto(
        double hp,
        @JsonProperty("hpperlevel") double hpPerLevel,
        double mp,
        @JsonProperty("mpperlevel") double mpPerLevel,
        double movespeed,
        double armor,
        @JsonProperty("armorperlevel") double armorPerLevel,
        double spellblock,
        @JsonProperty("spellblockperlevel") double spellblockPerLevel,
        double attackrange,
        double hpregen,
        @JsonProperty("hpregenperlevel") double hpregenPerLevel,
        double mpregen,
        @JsonProperty("mpregenperlevel") double mpregenPerLevel,
        double crit,
        @JsonProperty("critperlevel") double critPerLevel,
        double attackdamage,
        @JsonProperty("attackdamageperlevel") double attackdamagePerLevel,
        @JsonProperty("attackspeedperlevel") double attackspeedPerLevel,
        double attackspeed
) {
}
