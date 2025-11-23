package core.dto.matchV5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Team(
        int teamId,
        boolean win,
        Objectives objectives
) {}
