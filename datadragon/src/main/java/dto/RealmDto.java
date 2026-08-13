package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RealmDto(
        @JsonProperty("n") Map<String, String> versions,
        @JsonProperty("v") String version,
        @JsonProperty("l") String locale,
        String cdn,
        @JsonProperty("dd") String dataDragonVersion,
        @JsonProperty("lg") String legacyVersion,
        @JsonProperty("css") String cssVersion,
        @JsonProperty("profileiconmax") Integer profileIconMax,
        Object store
) {
}
