package core.dto.matchV5;

import java.util.Map;

public record ParticipantFramesDto(
        Map<String, ParticipantFrameDto> frame
) {
}