package core.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collections;

/** Optional filters for the Match-V5 match-id history endpoint. */
@Getter
public final class MatchHistoryQuery {
    private final int start;
    private final int count;
    private final Instant startTime;
    private final Instant endTime;
    private final Integer queue;
    private final String type;

    @Builder
    private MatchHistoryQuery(
            final Integer start,
            final Integer count,
            final Instant startTime,
            final Instant endTime,
            final Integer queue,
            final String type
    ) {
        this.start = start == null ? 0 : start;
        this.count = count == null ? 20 : count;
        this.startTime = startTime;
        this.endTime = endTime;
        this.queue = queue;
        this.type = type;

        if (this.start < 0) {
            throw new IllegalArgumentException("Match history start must not be negative");
        }
        if (this.count < 1 || this.count > 100) {
            throw new IllegalArgumentException("Match history count must be between 1 and 100");
        }
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Match history startTime must not be after endTime");
        }
    }

    public static MatchHistoryQuery firstPage(final int count) {
        return builder().count(count).build();
    }

    public Map<String, String> queryParameters() {
        final Map<String, String> parameters = new LinkedHashMap<>();
        if (start > 0) {
            parameters.put("start", Integer.toString(start));
        }
        parameters.put("count", Integer.toString(count));
        if (startTime != null) {
            parameters.put("startTime", Long.toString(startTime.getEpochSecond()));
        }
        if (endTime != null) {
            parameters.put("endTime", Long.toString(endTime.getEpochSecond()));
        }
        if (queue != null) {
            parameters.put("queue", Integer.toString(queue));
        }
        if (type != null && !type.isBlank()) {
            parameters.put("type", type);
        }
        return Collections.unmodifiableMap(parameters);
    }
}
