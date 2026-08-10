package core.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import core.config.Regions;
import core.dto.summoner.SummonerDto;
import core.http.ApiResponse;
import core.http.RiotHttp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class SummonerClientTest {

    @Mock
    private RiotHttp riotHttp;

    private SummonerClient summonerClient;

    @BeforeEach
    void setUp() {
        summonerClient = new SummonerClient(riotHttp);
    }

    @Test
    void byPuuid_shouldReturnSummonerDto() {
        String puuid = "test-puuid";
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.EUW1;

        SummonerDto expected = new SummonerDto(
                "id123",
                "id456",
                puuid,
                "TestSummoner",
                1234,
                1L,
                150L
        );

        ApiResponse<SummonerDto> response = new ApiResponse<>(200, Map.of(), expected);
        when(riotHttp.get(any(URI.class), eq(SummonerDto.class))).thenReturn(response);

        SummonerDto result = summonerClient.byPuuid(platformRegion, puuid);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("id123");
        assertThat(result.accountId()).isEqualTo("id456");
        assertThat(result.puuid()).isEqualTo(puuid);
        assertThat(result.name()).isEqualTo("TestSummoner");
        assertThat(result.profileIconId()).isEqualTo(1234);
        assertThat(result.revisionDate()).isEqualTo(1L);
        assertThat(result.summonerLevel()).isEqualTo(150L);

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/summoner/v4/summoners/by-puuid/" + puuid)),
                eq(SummonerDto.class)
        );
    }
}
