package core.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import core.config.Regions;
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
class MatchV5ClientTest {

    @Mock
    private RiotHttp riotHttp;

    private MatchV5Client matchV5Client;

    @BeforeEach
    void setUp() {
        matchV5Client = new MatchV5Client(riotHttp);
    }

    @Test
    void isByPuuid_shouldReturnArrayOfMatchIds() {
        String puuid = "test-puuid";
        int start = 0;
        int count = 20;
        Regions.RegionalRoute route = Regions.RegionalRoute.EUROPE;

        String[] expectedMatchIds = new String[]{
                "EUW1_1234567890",
                "EUW1_0987654321",
                "EUW1_1122334455"
        };

        ApiResponse<String[]> response = new ApiResponse<>(200, Map.of(), expectedMatchIds);
        when(riotHttp.get(any(URI.class), eq(String[].class))).thenReturn(response);

        String[] result = matchV5Client.idsByPuuid(route, puuid, start, count);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(
                "EUW1_1234567890",
                "EUW1_0987654321",
                "EUW1_1122334455");

        verify(riotHttp).get(
                eq(URI.create("https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=" + start + "&count=" + count)),
                eq(String[].class)
        );
    }
}
