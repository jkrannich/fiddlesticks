package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChampionStatsDto(
        double hp,
        double hpPerLevel,
        double mp,
        double mpPerLevel,
        double movespeed,
        double armor,
        double armorPerLevel,
        double spellblock,
        double spellblockPerLevel,
        double attackrange,
        double hpregen,
        double hpregenPerLevel,
        double mpregen,
        double mpregenPerLevel,
        double crit,
        double critPerLevel,
        double attackdamage,
        double attackdamagePerLevel,
        double attackspeedPerLevel,
        double attackspeed
) {
}
