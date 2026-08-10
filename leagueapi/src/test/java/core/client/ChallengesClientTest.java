package core.client;

import com.fasterxml.jackson.core.type.TypeReference;
import core.config.Regions;
import core.dto.challenges.ApexPlayerInfoDto;
import core.dto.challenges.ChallengeConfigInfoDto;
import core.dto.challenges.ChallengeInfoDto;
import core.dto.challenges.ChallengePointDto;
import core.dto.challenges.PlayerClientPreferencesDto;
import core.dto.challenges.PlayerInfoDto;
import core.enums.Level;
import core.enums.State;
import core.enums.Tracking;
import core.http.ApiResponse;
import core.http.RiotHttp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChallengesClientTest {

    @Mock
    private RiotHttp riotHttp;

    private ChallengesClient challengesClient;

    @BeforeEach
    void setUp() {
        challengesClient = new ChallengesClient(riotHttp);
    }

    @Test
    void listAllBasicChallengeConfigInfo_shouldReturnListOfChallengeConfigInfoDto() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.EUW1;

        ChallengeConfigInfoDto[] configs = new ChallengeConfigInfoDto[]{
                new ChallengeConfigInfoDto(
                        1L,
                        Map.of("en_US", Map.of("name", "Challenge 1")),
                        State.ENABLED,
                        Tracking.LIFETIME,
                        1000000L,
                        2000000L,
                        true,
                        Map.of("IRON", 10.0, "BRONZE", 50.0)
                ),
                new ChallengeConfigInfoDto(
                        2L,
                        Map.of("en_US", Map.of("name", "Challenge 2")),
                        State.ENABLED,
                        Tracking.SEASON,
                        1500000L,
                        2500000L,
                        false,
                        Map.of("SILVER", 100.0, "GOLD", 200.0)
                )
        };

        ApiResponse<ChallengeConfigInfoDto[]> response = new ApiResponse<>(200, Map.of(), configs);
        when(riotHttp.get(any(URI.class), eq(ChallengeConfigInfoDto[].class))).thenReturn(response);

        List<ChallengeConfigInfoDto> result = challengesClient.listAllBasicChallengeConfigInfo(platformRegion);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).state()).isEqualTo(State.ENABLED);
        assertThat(result.get(0).tracking()).isEqualTo(Tracking.LIFETIME);
        assertThat(result.get(0).leaderboard()).isTrue();
        assertThat(result.get(1).id()).isEqualTo(2L);
        assertThat(result.get(1).tracking()).isEqualTo(Tracking.SEASON);
        assertThat(result.get(1).leaderboard()).isFalse();

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/challenges/v1/challenges/config")),
                eq(ChallengeConfigInfoDto[].class)
        );
    }

    @Test
    void getMapOfLevelToPercentileOfPlayersWhoAchievedIt_shouldReturnMapForAllChallenges() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.NA1;

        Map<Long, Map<Integer, Map<Level, Double>>> expectedMap = Map.of(
                1L, Map.of(1, Map.of(Level.IRON, 10.5, Level.BRONZE, 25.3)),
                2L, Map.of(2, Map.of(Level.SILVER, 50.0, Level.GOLD, 75.2))
        );

        ApiResponse<Map> response = new ApiResponse<>(200, Map.of(), expectedMap);
        doReturn(response).when(riotHttp).get(any(URI.class), any(TypeReference.class));

        Map<Long, Map<Integer, Map<Level, Double>>> result =
                challengesClient.getMapOfLevelToPercentileOfPlayersWhoAchievedIt(platformRegion);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsKey(1L);
        assertThat(result).containsKey(2L);

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/challenges/v1/challenges/percentiles")),
                any(TypeReference.class)
        );
    }

    @Test
    void getChallengeConfig_shouldReturnChallengeConfigInfoDto() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.KR;
        int challengeId = 12345;

        ChallengeConfigInfoDto config = new ChallengeConfigInfoDto(
                12345L,
                Map.of("ko_KR", Map.of("name", "Korean Challenge")),
                State.ENABLED,
                Tracking.LIFETIME,
                1234567890L,
                9876543210L,
                true,
                Map.of("MASTER", 500.0, "GRANDMASTER", 1000.0, "CHALLENGER", 2000.0)
        );

        ApiResponse<ChallengeConfigInfoDto> response = new ApiResponse<>(200, Map.of(), config);
        when(riotHttp.get(any(URI.class), eq(ChallengeConfigInfoDto.class))).thenReturn(response);

        ChallengeConfigInfoDto result = challengesClient.getChallengeConfig(platformRegion, challengeId);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(12345L);
        assertThat(result.state()).isEqualTo(State.ENABLED);
        assertThat(result.tracking()).isEqualTo(Tracking.LIFETIME);
        assertThat(result.leaderboard()).isTrue();
        assertThat(result.threshholds()).containsKeys("MASTER", "GRANDMASTER", "CHALLENGER");

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/challenges/v1/challenges/" + challengeId + "/config")),
                eq(ChallengeConfigInfoDto.class)
        );
    }

    @Test
    void getTopPlayersForEachlevel_shouldReturnListOfApexPlayerInfoDto() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.BR1;
        Level level = Level.CHALLENGER;
        int challengeId = 999;

        ApexPlayerInfoDto[] apexPlayers = new ApexPlayerInfoDto[]{
                new ApexPlayerInfoDto("puuid-1", 9999.99, 1),
                new ApexPlayerInfoDto("puuid-2", 8888.88, 2),
                new ApexPlayerInfoDto("puuid-3", 7777.77, 3)
        };

        ApiResponse<ApexPlayerInfoDto[]> response = new ApiResponse<>(200, Map.of(), apexPlayers);
        when(riotHttp.get(any(URI.class), eq(ApexPlayerInfoDto[].class))).thenReturn(response);

        List<ApexPlayerInfoDto> result = challengesClient.getTopPlayersForEachlevel(
                platformRegion, level, challengeId
        );

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result.get(0).puuid()).isEqualTo("puuid-1");
        assertThat(result.get(0).value()).isEqualTo(9999.99);
        assertThat(result.get(0).position()).isEqualTo(1);
        assertThat(result.get(1).position()).isEqualTo(2);
        assertThat(result.get(2).position()).isEqualTo(3);

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/challenges/v1/challenges/" + challengeId + "/leaderboards/by-level/" + level.name())),
                eq(ApexPlayerInfoDto[].class)
        );
    }

    @Test
    void getMapOfLevelToPercentileOfPlayersWhoAchievedIt_withChallengeId_shouldReturnMapForSpecificChallenge() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.EUN1;
        int challengeId = 555;

        Map<Level, Double> expectedMap = Map.of(
                Level.IRON, 5.5,
                Level.BRONZE, 15.3,
                Level.SILVER, 35.7,
                Level.GOLD, 60.2,
                Level.PLATINUM, 80.9
        );

        ApiResponse<Map> response = new ApiResponse<>(200, Map.of(), expectedMap);
        doReturn(response).when(riotHttp).get(any(URI.class), any(TypeReference.class));

        Map<Level, Double> result = challengesClient.getMapOfLevelToPercentileOfPlayersWhoAchievedIt(
                platformRegion, challengeId
        );

        assertThat(result).isNotNull();
        assertThat(result).hasSize(5);
        assertThat(result.get(Level.IRON)).isEqualTo(5.5);
        assertThat(result.get(Level.BRONZE)).isEqualTo(15.3);
        assertThat(result.get(Level.PLATINUM)).isEqualTo(80.9);

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/challenges/v1/challenges/" + challengeId + "/percentiles")),
                any(TypeReference.class)
        );
    }

    @Test
    void getPlayerInformationWithListOfAllPrgoressedChallenges_shouldReturnPlayerInfoDto() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.OC1;
        String puuid = "test-player-puuid";

        ChallengeInfoDto challenge1 = new ChallengeInfoDto(
                75.5, 1000, 1234567890L, 150.0, 1L, Level.GOLD, 500
        );
        ChallengeInfoDto challenge2 = new ChallengeInfoDto(
                90.2, 500, 1234567891L, 250.0, 2L, Level.PLATINUM, 200
        );

        PlayerClientPreferencesDto preferences = new PlayerClientPreferencesDto(
                "accent-1",
                "Master Player",
                List.of("1", "2", "3"),
                "border-1",
                5
        );

        ChallengePointDto totalPoints = new ChallengePointDto("PLATINUM", 5000L, 10000L, 85L);
        Map<String, ChallengePointDto> categoryPoints = Map.of(
                "TEAMWORK", new ChallengePointDto("GOLD", 1000L, 2000L, 70L),
                "EXPERTISE", new ChallengePointDto("PLATINUM", 1500L, 3000L, 80L)
        );

        PlayerInfoDto playerInfo = new PlayerInfoDto(
                List.of(challenge1, challenge2),
                preferences,
                totalPoints,
                categoryPoints
        );

        ApiResponse<PlayerInfoDto> response = new ApiResponse<>(200, Map.of(), playerInfo);
        when(riotHttp.get(any(URI.class), eq(PlayerInfoDto.class))).thenReturn(response);

        PlayerInfoDto result = challengesClient.getPlayerInformationWithListOfAllPrgoressedChallenges(
                platformRegion, puuid
        );

        assertThat(result).isNotNull();
        assertThat(result.challenges()).hasSize(2);
        assertThat(result.challenges().get(0).challengeId()).isEqualTo(1L);
        assertThat(result.challenges().get(0).level()).isEqualTo(Level.GOLD);
        assertThat(result.challenges().get(1).challengeId()).isEqualTo(2L);
        assertThat(result.challenges().get(1).level()).isEqualTo(Level.PLATINUM);
        assertThat(result.preferences().title()).isEqualTo("Master Player");
        assertThat(result.preferences().prestigeCrestBorderLevel()).isEqualTo(5);
        assertThat(result.totalPoints().level()).isEqualTo("PLATINUM");
        assertThat(result.totalPoints().current()).isEqualTo(5000L);
        assertThat(result.categoryPoints()).hasSize(2);
        assertThat(result.categoryPoints()).containsKeys("TEAMWORK", "EXPERTISE");

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/challenges/v1/players/" + puuid)),
                eq(PlayerInfoDto.class)
        );
    }
}
