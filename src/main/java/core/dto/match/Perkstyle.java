package core.dto.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Perkstyle(
        String description,
        List<PerkStyleSelection> selection,
        Integer style
) {
}
