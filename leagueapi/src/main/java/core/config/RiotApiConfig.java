package core.config;

import lombok.Getter;
import lombok.Builder;

import java.time.Duration;

/** Configuration for the simple synchronous Riot API client. */
@Getter
public final class RiotApiConfig {
    private final String apiKey;
    private final Regions.PlatformRegion defaultPlatformRegion;
    private final Regions.RegionalRoute defaultRegionalRoute;
    private final Duration timeout;

    @Builder
    private RiotApiConfig(
            final String apiKey,
            final Regions.PlatformRegion defaultPlatformRegion,
            final Regions.RegionalRoute defaultRegionalRoute,
            final Duration timeout
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("Riot API key must not be blank");
        }
        this.apiKey = apiKey;
        this.defaultPlatformRegion = defaultPlatformRegion == null
                ? Regions.PlatformRegion.EUW1
                : defaultPlatformRegion;
        this.defaultRegionalRoute = defaultRegionalRoute == null
                ? Regions.RegionalRoute.EUROPE
                : defaultRegionalRoute;
        this.timeout = timeout == null ? Duration.ofSeconds(10) : timeout;
        if (this.timeout.isNegative() || this.timeout.isZero()) {
            throw new IllegalArgumentException("Timeout must be positive");
        }
    }

    public static RiotApiConfig of(final String apiKey) {
        return builder().apiKey(apiKey).build();
    }

    public static RiotApiConfig of(
            final String apiKey,
            final Regions.PlatformRegion platformRegion,
            final Regions.RegionalRoute regionalRoute
    ) {
        return builder()
                .apiKey(apiKey)
                .defaultPlatformRegion(platformRegion)
                .defaultRegionalRoute(regionalRoute)
                .build();
    }
}
