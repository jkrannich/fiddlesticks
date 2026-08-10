package core.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import core.config.Regions;
import core.dto.championRotation.ChampionInfoDto;
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
class ChampionClientTest {
    @Mock
    private RiotHttp riotHttp;

    private ChampionClient championClient;

    @BeforeEach
    void setUp() {
        championClient = new ChampionClient(riotHttp);
    }

    @Test
    void getChampionRotation_shouldReturnChampionInfoDto() {
        Regions.PlatformRegion platform = Regions.PlatformRegion.EUW1;

        ChampionInfoDto expected = new ChampionInfoDto(
                10,
                List.of(1,2,3),
                List.of(244, 222, 333)
        );

        ApiResponse<ChampionInfoDto> response = new ApiResponse<>(200, Map.of(), expected);
        when(riotHttp.get(any(URI.class), eq(ChampionInfoDto.class))).thenReturn(response);

        ChampionInfoDto result = championClient.getChampionRotation(platform);

        assertThat(result).isNotNull();
        assertThat(result.maxNewPlayerLevel()).isEqualTo(expected.maxNewPlayerLevel());
        assertThat(result.freeChampionIdsForNewPlayers()).isEqualTo(expected.freeChampionIdsForNewPlayers());
        assertThat(result.freeChampionIds()).isEqualTo(expected.freeChampionIds());

        verify(riotHttp).get(
                eq(URI.create("https://euw1.api.riotgames.com/lol/platform/v3/champion-rotations")),
                eq(ChampionInfoDto.class)
        );
    }
}
