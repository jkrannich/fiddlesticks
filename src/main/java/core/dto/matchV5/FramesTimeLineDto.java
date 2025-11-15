package core.dto.matchV5;

import java.util.List;

public record FramesTimeLineDto(
        List<EventsTimeLineDto> events,
        ParticipantFramesDto participantFrames,
        int timestamp
) {
}
