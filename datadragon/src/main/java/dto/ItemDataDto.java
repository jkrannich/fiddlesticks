package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDataDto(
        String name,
        String description,
        String plaintext,
        Map<String, Boolean> into,
        List<String> from,
        ItemGoldDto gold,
        Map<String, Integer> stats
) {
}
