package core.error;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class RiotRateLimitException extends RiotException {
    private final Instant retryAfter;

    public RiotRateLimitException(final String message, final Instant retryAfter) {
        super(message);
        this.retryAfter = retryAfter;
    }

    public Instant retryAfter() {
        return retryAfter;
    }

    public static RiotRateLimitException fromHeaders(Map<String, List<String>> headers) {
        final String retryAfterHeader = headers.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase("Retry-After"))
                .flatMap(entry -> entry.getValue().stream())
                .findFirst()
                .orElse(null);

        Instant retryAfter = Instant.now().plusSeconds(1);
        if (retryAfterHeader != null) {
            try {
                retryAfter = Instant.now().plusSeconds(Long.parseLong(retryAfterHeader));
            } catch (NumberFormatException ignored) {
                // Keep the one-second fallback.
            }
        }
        return new RiotRateLimitException("Rate limited", retryAfter);
    }
}
