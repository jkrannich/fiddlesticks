package core.client;

import com.fasterxml.jackson.core.type.TypeReference;
import core.config.Regions;
import core.dto.challenges.ApexPlayerInfoDto;
import core.dto.challenges.ChallengeConfigInfoDto;
import core.dto.challenges.PlayerInfoDto;
import core.enums.Level;
import core.http.RiotHttp;
import core.http.RiotUriBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

/** Thin LoL-Challenges-V1 endpoint client. */
public final class ChallengesClient {
    private static final TypeReference<Map<Long, Map<Integer, Map<Level, Double>>>> ALL_PERCENTILES_TYPE =
            new TypeReference<>() { };
    private static final TypeReference<Map<Level, Double>> CHALLENGE_PERCENTILES_TYPE =
            new TypeReference<>() { };

    private final RiotHttp riotHttp;
    private final Regions.PlatformRegion defaultRegion;

    public ChallengesClient(final RiotHttp riotHttp) {
        this(riotHttp, null);
    }

    public ChallengesClient(final RiotHttp riotHttp, final Regions.PlatformRegion defaultRegion) {
        this.riotHttp = riotHttp;
        this.defaultRegion = defaultRegion;
    }

    public List<ChallengeConfigInfoDto> listAllBasicChallengeConfigInfo() {
        return listAllBasicChallengeConfigInfo(requireDefaultRegion());
    }

    public List<ChallengeConfigInfoDto> listAllBasicChallengeConfigInfo(final Regions.PlatformRegion platformRegion) {
        final URI uri = RiotUriBuilder.path(platformRegion.baseUrl(), "lol", "challenges", "v1", "challenges", "config");
        return List.of(riotHttp.get(uri, ChallengeConfigInfoDto[].class).body());
    }

    public Map<Long, Map<Integer, Map<Level, Double>>> getMapOfLevelToPercentileOfPlayersWhoAchievedIt() {
        return getMapOfLevelToPercentileOfPlayersWhoAchievedIt(requireDefaultRegion());
    }

    public Map<Long, Map<Integer, Map<Level, Double>>> getMapOfLevelToPercentileOfPlayersWhoAchievedIt(
            final Regions.PlatformRegion platformRegion
    ) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(), "lol", "challenges", "v1", "challenges", "percentiles"
        );
        return riotHttp.get(uri, ALL_PERCENTILES_TYPE).body();
    }

    public ChallengeConfigInfoDto getChallengeConfig(final int challengeId) {
        return getChallengeConfig(requireDefaultRegion(), challengeId);
    }

    public ChallengeConfigInfoDto getChallengeConfig(final Regions.PlatformRegion platformRegion, final int challengeId) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(), "lol", "challenges", "v1", "challenges", Integer.toString(challengeId), "config"
        );
        return riotHttp.get(uri, ChallengeConfigInfoDto.class).body();
    }

    public List<ApexPlayerInfoDto> getTopPlayersForEachlevel(final Level level, final int challengeId) {
        return getTopPlayersForEachlevel(requireDefaultRegion(), level, challengeId);
    }

    public List<ApexPlayerInfoDto> getTopPlayersForEachLevel(final Level level, final int challengeId) {
        return getTopPlayersForEachlevel(level, challengeId);
    }

    public List<ApexPlayerInfoDto> getTopPlayersForEachlevel(
            final Regions.PlatformRegion platformRegion,
            final Level level,
            final int challengeId
    ) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(), "lol", "challenges", "v1", "challenges",
                Integer.toString(challengeId), "leaderboards", "by-level", level.name()
        );
        return List.of(riotHttp.get(uri, ApexPlayerInfoDto[].class).body());
    }

    public Map<Level, Double> getMapOfLevelToPercentileOfPlayersWhoAchievedIt(final int challengeId) {
        return getMapOfLevelToPercentileOfPlayersWhoAchievedIt(requireDefaultRegion(), challengeId);
    }

    public Map<Level, Double> getMapOfLevelToPercentileOfPlayersWhoAchievedIt(
            final Regions.PlatformRegion platformRegion,
            final int challengeId
    ) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(), "lol", "challenges", "v1", "challenges",
                Integer.toString(challengeId), "percentiles"
        );
        return riotHttp.get(uri, CHALLENGE_PERCENTILES_TYPE).body();
    }

    public PlayerInfoDto getPlayerInformationWithListOfAllPrgoressedChallenges(final String puuid) {
        return getPlayerInformationWithListOfAllPrgoressedChallenges(requireDefaultRegion(), puuid);
    }

    public PlayerInfoDto getPlayerInformationWithProgressedChallenges(final String puuid) {
        return getPlayerInformationWithListOfAllPrgoressedChallenges(puuid);
    }

    public PlayerInfoDto getPlayerInformationWithListOfAllPrgoressedChallenges(
            final Regions.PlatformRegion platformRegion,
            final String puuid
    ) {
        final URI uri = RiotUriBuilder.path(platformRegion.baseUrl(), "lol", "challenges", "v1", "players", puuid);
        return riotHttp.get(uri, PlayerInfoDto.class).body();
    }

    private Regions.PlatformRegion requireDefaultRegion() {
        if (defaultRegion == null) {
            throw new IllegalStateException("No default platform region configured; use the region overload or RiotApi.builder()");
        }
        return defaultRegion;
    }
}
