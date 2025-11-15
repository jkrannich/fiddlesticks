package core.dto.matchV5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Objectives(
        Objective baron,
        Objective dragon,
        Objective tower
) {}
