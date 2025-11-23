package core.dto.matchV5;

import java.util.List;

public record MetadataTimeLineDto(
        String dataVersion,
        String matchId,
        List<String> participants
) {
}
