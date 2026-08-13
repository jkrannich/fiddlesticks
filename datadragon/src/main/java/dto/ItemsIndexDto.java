package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemsIndexDto(
        String type,
        String version,
        ItemDataDto basic,
        Map<String, ItemDataDto> data
) {
}
