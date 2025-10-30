package core.dto.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PerkSelection(
        int perk,
        int var1,
        int var2,
        int var3
) {
}
