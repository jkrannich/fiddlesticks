package core.dto.matchV5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Metadata(
        String dataVersion,
        String matchId,
        List<String> participants
) {}