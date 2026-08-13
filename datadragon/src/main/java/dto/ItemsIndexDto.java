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

    /** Compatibility constructor for the original index DTO shape. */
    public ItemsIndexDto(
            final String type,
            final String version,
            final Map<String, ItemDataDto> data
    ) {
        this(type, version, null, data);
    }
}
