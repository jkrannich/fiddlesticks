package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDataDto(
        String name,
        String description,
        String colloq,
        String plaintext,
        List<String> into,
        List<String> from,
        ItemGoldDto gold,
        ItemRuneDto rune,
        ChampionImageDto image,
        List<String> tags,
        Map<String, Boolean> maps,
        Map<String, Double> stats,
        Map<String, String> effect,
        Boolean consumed,
        Integer stacks,
        Integer depth,
        Boolean consumeOnFull,
        Integer specialRecipe,
        Boolean inStore,
        Boolean hideFromAll,
        String requiredChampion,
        String requiredAlly
) {

    /** Compatibility constructor for the original item DTO shape. */
    public ItemDataDto(
            final String name,
            final String description,
            final String plaintext,
            final List<String> into,
            final List<String> from,
            final ItemGoldDto gold,
            final Map<String, Double> stats
    ) {
        this(
                name,
                description,
                null,
                plaintext,
                into,
                from,
                gold,
                null,
                null,
                null,
                null,
                stats,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
