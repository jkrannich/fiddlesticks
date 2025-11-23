package core.error;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class RiotRateLimitException extends RiotException {
    public final Instant retryAfter;
    public RiotRateLimitException(String message, Instant retryAfter) {
        super(message);
        this.retryAfter = retryAfter;
    }
    public static RiotRateLimitException fromHeaders(Map<String, List<String>> headers) {
        Instant retryAfter = headers.getOrDefault("Retry-after", List.of()).stream().findFirst()
                .map(Long::parseLong).map(sec -> Instant.now().plusSeconds(sec)).orElse(Instant.now().plusSeconds(1));
        return new RiotRateLimitException("Rate limited", retryAfter);
    }
}
