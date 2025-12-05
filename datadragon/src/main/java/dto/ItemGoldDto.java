package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemGoldDto(
        int base,
        int total,
        int sell,
        boolean purchasable
) {
}
