package core.dto.matchV5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PerkStatPerks(
        Integer defense,
        Integer offense,
        Integer flex
) {}
