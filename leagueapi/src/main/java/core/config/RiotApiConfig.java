package core.config;

import java.time.Duration;

public record RiotApiConfig(
        String apiKey,
        Regions.PlatformRegion platformRegion,
        Regions.RegionalRoute regionalRoute,
        Duration timeout
) {
    public static RiotApiConfig of(final String apiKey, final Regions.PlatformRegion platformRegion, final Regions.RegionalRoute regionalRoute) {
        return new RiotApiConfig(apiKey, platformRegion, regionalRoute, Duration.ofSeconds(10));
    }
}
