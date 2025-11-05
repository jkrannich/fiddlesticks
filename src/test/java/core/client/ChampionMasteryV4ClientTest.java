package core.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import core.config.Regions;
import core.dto.championMastery.ChampionMasteryDto;
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

@ExtendWith(MockitoExtension.class)
class ChampionMasteryV4ClientTest {

    @Mock
    private RiotHttp riotHttp;

    private ChampionMasteryV4Client championMasteryV4Client;

    @BeforeEach
    void setUp() {
        championMasteryV4Client = new ChampionMasteryV4Client(riotHttp);
    }

    @Test
    void getChampionMasteriesByPuuid_shouldReturnListOfChampionMasteryDto() {
        String puuid = "test-puuid";
        ChampionMasteryDto[] masteries = new ChampionMasteryDto[]{
                new ChampionMasteryDto(puuid, 1, false, 2L, 500L, 20, 2),
                new ChampionMasteryDto(puuid, 2, true, 3L, 500L, 2000, 2)
        };

        ApiResponse<ChampionMasteryDto[]> response = new ApiResponse<>(200, Map.of(), masteries);
        when(riotHttp.get(any(URI.class), eq(ChampionMasteryDto[].class))).thenReturn(response);

        List<ChampionMasteryDto> result = championMasteryV4Client.getChampionMasteriesByPuuid(
                Regions.PlatformRegion.EUW1,
                puuid
        );

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).championLevel()).isEqualTo(20);
        assertThat(result.get(1).championLevel()).isEqualTo(2000);

        verify(riotHttp).get(
                eq(URI.create("https://euw1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid)),
                eq(ChampionMasteryDto[].class)
        );
    }

    @Test
    void getChampionMasteriesByPuuidTop_shouldReturnListOfChampionMasteryDto() {
        String puuid = "test-puuid";
        ChampionMasteryDto[] masteries = new ChampionMasteryDto[]{
                new ChampionMasteryDto(puuid, 1, false, 2L, 500L, 20, 2),
                new ChampionMasteryDto(puuid, 2, true, 3L, 500L, 2000, 2)
        };

        ApiResponse<ChampionMasteryDto[]> response = new ApiResponse<>(200, Map.of(), masteries);
        when(riotHttp.get(any(URI.class), eq(ChampionMasteryDto[].class))).thenReturn(response);

        List<ChampionMasteryDto> result = championMasteryV4Client.getChampionMasteriesByPuuidTop(
                Regions.PlatformRegion.EUW1,
                puuid
        );

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).championLevel()).isEqualTo(20);
        assertThat(result.get(1).championLevel()).isEqualTo(2000);

        verify(riotHttp).get(
                eq(URI.create("https://euw1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid + "/top")),
                eq(ChampionMasteryDto[].class)
        );
    }
}
