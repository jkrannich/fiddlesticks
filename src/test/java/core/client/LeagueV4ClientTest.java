package core.client;

import core.config.Regions;
import core.dto.leagueExp.LeagueEntryDto;
import core.dto.leagueV4.LeagueItemDto;
import core.dto.leagueV4.LeagueListDto;
import core.enums.Division;
import core.enums.Queue;
import core.enums.Tier;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeagueV4ClientTest {

    @Mock
    private RiotHttp riotHttp;

    private LeagueV4Client leagueV4Client;

    @BeforeEach
    void setUp() {
        leagueV4Client = new LeagueV4Client(riotHttp);
    }

    @Test
    void getChallengerLeagueForGivenQueue_shouldReturnLeagueListDto() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.EUW1;
        Queue queueType = Queue.RANKED_SOLO_5x5;

        LeagueItemDto item1 = new LeagueItemDto(true, 100, null, false, true, false, "I", 450, 80, "puuid-1");
        LeagueItemDto item2 = new LeagueItemDto(false, 95, null, false, false, true, "I", 420, 75, "puuid-2");

        LeagueListDto expectedLeague = new LeagueListDto(
                "challenger-league-1",
                List.of(item1, item2),
                "CHALLENGER",
                "Challenger League",
                "RANKED_SOLO_5x5"
        );

        ApiResponse<LeagueListDto> response = new ApiResponse<>(200, Map.of(), expectedLeague);
        when(riotHttp.get(any(URI.class), eq(LeagueListDto.class))).thenReturn(response);

        LeagueListDto result = leagueV4Client.getChallengerLeagueForGivenQueue(platformRegion, queueType);

        assertThat(result).isNotNull();
        assertThat(result.leagueId()).isEqualTo("challenger-league-1");
        assertThat(result.tier()).isEqualTo("CHALLENGER");
        assertThat(result.name()).isEqualTo("Challenger League");
        assertThat(result.queue()).isEqualTo("RANKED_SOLO_5x5");
        assertThat(result.entries()).hasSize(2);
        assertThat(result.entries().get(0).leaguePoints()).isEqualTo(450);
        assertThat(result.entries().get(1).leaguePoints()).isEqualTo(420);

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/league/v4/challengerleagues/by-queue/" + queueType.name())),
                eq(LeagueListDto.class)
        );
    }

    @Test
    void getLeagueEntriesInAllQueuesByPuuid_shouldReturnSetOfLeagueEntryDto() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.NA1;
        String puuid = "test-puuid-123";

        LeagueEntryDto[] entries = new LeagueEntryDto[]{
                new LeagueEntryDto(
                        "league-1", "summoner-1", puuid, "RANKED_SOLO_5x5",
                        "GOLD", "III", 65, 120, 110, false, true, false, false, null
                ),
                new LeagueEntryDto(
                        "league-2", "summoner-1", puuid, "RANKED_FLEX_SR",
                        "SILVER", "I", 85, 90, 85, true, false, false, false, null
                )
        };

        ApiResponse<LeagueEntryDto[]> response = new ApiResponse<>(200, Map.of(), entries);
        when(riotHttp.get(any(URI.class), eq(LeagueEntryDto[].class))).thenReturn(response);

        Set<LeagueEntryDto> result = leagueV4Client.getLeagueEntriesInAllQueuesByPuuid(platformRegion, puuid);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(LeagueEntryDto::queueType)
                .containsExactlyInAnyOrder("RANKED_SOLO_5x5", "RANKED_FLEX_SR");
        assertThat(result).extracting(LeagueEntryDto::tier)
                .containsExactlyInAnyOrder("GOLD", "SILVER");
        assertThat(result).extracting(LeagueEntryDto::leaguePoints)
                .containsExactlyInAnyOrder(65, 85);

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/league/v4/entries/by-puuid/" + puuid)),
                eq(LeagueEntryDto[].class)
        );
    }

    @Test
    void getAllLeagueEntries_shouldReturnSetOfLeagueEntryDto() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.EUW1;
        Queue queueType = Queue.RANKED_SOLO_5x5;
        Tier tier = Tier.PLATINUM;
        Division division = Division.II;
        int page = 1;

        LeagueEntryDto[] entries = new LeagueEntryDto[]{
                new LeagueEntryDto(
                        "league-1", "summoner-1", "puuid-1", "RANKED_SOLO_5x5",
                        "PLATINUM", "II", 75, 100, 95, true, false, false, false, null
                ),
                new LeagueEntryDto(
                        "league-2", "summoner-2", "puuid-2", "RANKED_SOLO_5x5",
                        "PLATINUM", "II", 50, 80, 75, false, true, false, false, null
                ),
                new LeagueEntryDto(
                        "league-3", "summoner-3", "puuid-3", "RANKED_SOLO_5x5",
                        "PLATINUM", "II", 90, 110, 100, false, false, true, false, null
                )
        };

        ApiResponse<LeagueEntryDto[]> response = new ApiResponse<>(200, Map.of(), entries);
        when(riotHttp.get(any(URI.class), eq(LeagueEntryDto[].class))).thenReturn(response);

        Set<LeagueEntryDto> result = leagueV4Client.getAllLeagueEntries(platformRegion, queueType, tier, division, page);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).extracting(LeagueEntryDto::summonerId)
                .containsExactlyInAnyOrder("summoner-1", "summoner-2", "summoner-3");
        assertThat(result).extracting(LeagueEntryDto::leaguePoints)
                .containsExactlyInAnyOrder(75, 50, 90);
        assertThat(result).extracting(LeagueEntryDto::tier)
                .containsOnly("PLATINUM");
        assertThat(result).extracting(LeagueEntryDto::rank)
                .containsOnly("II");

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/league/v4/entries/"
                        + queueType.name() + "/" + tier.name() + "/" + division.name() + "?page=" + page)),
                eq(LeagueEntryDto[].class)
        );
    }

    @Test
    void getGrandMasterLeagueForGivenQueue_shouldReturnLeagueListDto() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.KR;
        Queue queueType = Queue.RANKED_FLEX_SR;

        LeagueItemDto item1 = new LeagueItemDto(false, 150, null, false, true, true, "I", 550, 120, "puuid-gm-1");
        LeagueItemDto item2 = new LeagueItemDto(true, 145, null, false, false, false, "I", 520, 115, "puuid-gm-2");

        LeagueListDto expectedLeague = new LeagueListDto(
                "grandmaster-league-1",
                List.of(item1, item2),
                "GRANDMASTER",
                "GrandMaster League",
                "RANKED_FLEX_SR"
        );

        ApiResponse<LeagueListDto> response = new ApiResponse<>(200, Map.of(), expectedLeague);
        when(riotHttp.get(any(URI.class), eq(LeagueListDto.class))).thenReturn(response);

        LeagueListDto result = leagueV4Client.getGrandMasterLeagueForGivenQueue(platformRegion, queueType);

        assertThat(result).isNotNull();
        assertThat(result.leagueId()).isEqualTo("grandmaster-league-1");
        assertThat(result.tier()).isEqualTo("GRANDMASTER");
        assertThat(result.name()).isEqualTo("GrandMaster League");
        assertThat(result.queue()).isEqualTo("RANKED_FLEX_SR");
        assertThat(result.entries()).hasSize(2);
        assertThat(result.entries().get(0).wins()).isEqualTo(150);
        assertThat(result.entries().get(1).wins()).isEqualTo(145);

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/league/v4/grandmasterleagues/by-queue/" + queueType.name())),
                eq(LeagueListDto.class)
        );
    }

    @Test
    void getLeagueWithGivenIdIncludingInactiveEntries_shouldReturnLeagueListDto() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.BR1;
        String leagueId = "specific-league-id-456";

        LeagueItemDto item1 = new LeagueItemDto(true, 60, null, false, false, false, "III", 45, 55, "puuid-active");
        LeagueItemDto item2 = new LeagueItemDto(false, 30, null, true, true, false, "III", 20, 28, "puuid-inactive");

        LeagueListDto expectedLeague = new LeagueListDto(
                leagueId,
                List.of(item1, item2),
                "GOLD",
                "Gold League III",
                "RANKED_SOLO_5x5"
        );

        ApiResponse<LeagueListDto> response = new ApiResponse<>(200, Map.of(), expectedLeague);
        when(riotHttp.get(any(URI.class), eq(LeagueListDto.class))).thenReturn(response);

        LeagueListDto result = leagueV4Client.getLeagueWithGivenIdIncludingInactiveEntries(platformRegion, leagueId);

        assertThat(result).isNotNull();
        assertThat(result.leagueId()).isEqualTo(leagueId);
        assertThat(result.tier()).isEqualTo("GOLD");
        assertThat(result.entries()).hasSize(2);
        assertThat(result.entries().get(0).inactive()).isFalse();
        assertThat(result.entries().get(1).inactive()).isTrue();
        assertThat(result.entries().get(0).freshBlood()).isTrue();
        assertThat(result.entries().get(1).veteran()).isTrue();

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/league/v4/leagues/" + leagueId)),
                eq(LeagueListDto.class)
        );
    }

    @Test
    void getMasterLeagueForGivenQueue_shouldReturnLeagueListDto() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.EUN1;
        Queue queueType = Queue.RANKED_SOLO_5x5;

        LeagueItemDto item1 = new LeagueItemDto(false, 200, null, false, true, false, "I", 350, 180, "puuid-master-1");
        LeagueItemDto item2 = new LeagueItemDto(true, 180, null, false, false, true, "I", 320, 160, "puuid-master-2");
        LeagueItemDto item3 = new LeagueItemDto(false, 190, null, false, true, false, "I", 340, 170, "puuid-master-3");

        LeagueListDto expectedLeague = new LeagueListDto(
                "master-league-1",
                List.of(item1, item2, item3),
                "MASTER",
                "Master League",
                "RANKED_SOLO_5x5"
        );

        ApiResponse<LeagueListDto> response = new ApiResponse<>(200, Map.of(), expectedLeague);
        when(riotHttp.get(any(URI.class), eq(LeagueListDto.class))).thenReturn(response);

        LeagueListDto result = leagueV4Client.getMasterLeagueForGivenQueue(platformRegion, queueType);

        assertThat(result).isNotNull();
        assertThat(result.leagueId()).isEqualTo("master-league-1");
        assertThat(result.tier()).isEqualTo("MASTER");
        assertThat(result.name()).isEqualTo("Master League");
        assertThat(result.queue()).isEqualTo("RANKED_SOLO_5x5");
        assertThat(result.entries()).hasSize(3);
        assertThat(result.entries()).extracting(LeagueItemDto::leaguePoints)
                .containsExactly(350, 320, 340);
        assertThat(result.entries()).extracting(LeagueItemDto::wins)
                .containsExactly(200, 180, 190);

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/league/v4/masterleagues/by-queue/" + queueType.name())),
                eq(LeagueListDto.class)
        );
    }
}