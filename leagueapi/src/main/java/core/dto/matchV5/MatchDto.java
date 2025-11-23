package core.dto.matchV5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MatchDto(Metadata metadata, Info info) {}


