package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RuneDto(
        int id,
        String key,
        String icon,
        String name,
        String shortDesc,
        String longDesc
) {
}
