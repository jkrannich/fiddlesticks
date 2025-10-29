package core.config;

import java.time.Duration;

public record RiotApiConfig(
        String apiKey,
        Regions.PlatformRegion platformRegion,
        Regions.RegionalRoute regionalRoute,
        Duration timeout
) {
    public static RiotApiConfig of(String apiKey, Regions.PlatformRegion platformRegion, Regions.RegionalRoute regionalRoute) {
        return new RiotApiConfig(apiKey, platformRegion, regionalRoute, Duration.ofSeconds(10));
    }
}
