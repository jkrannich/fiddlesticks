package core.dto.matchV5;

public record EventsTimeLineDto(
        long timestamp,
        long realTimestamp,
        String type
) {
}
