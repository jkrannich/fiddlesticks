package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RuneTreeDto(
        int id,
        String key,
        String icon,
        String name,
        List<RuneSlotDto> slots
) {
}
