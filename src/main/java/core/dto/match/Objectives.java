package core.dto.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Objectives(
        Objective baron,
        Objective dragon,
        Objective tower
) {}
