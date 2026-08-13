package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemRuneDto(
        @JsonProperty("isrune") boolean isRune,
        int tier,
        String type
) {
}
