package core.dto.matchV5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Perkstyle(
        String description,
        List<PerkStyleSelection> selection,
        Integer style
) {
}
