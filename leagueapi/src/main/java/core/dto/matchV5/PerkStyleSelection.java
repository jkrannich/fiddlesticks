package core.dto.matchV5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PerkStyleSelection(
        int perk,
        int var1,
        int var2,
        int var3
) {
}
