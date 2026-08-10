package core.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import core.config.Regions;
import core.dto.matchV5.MatchDto;
import core.dto.matchV5.TimelineDto;
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
class MatchClientTest {

    @Mock
    private RiotHttp riotHttp;

    private MatchClient matchClient;

    @BeforeEach
    void setUp() {
        matchClient = new MatchClient(riotHttp);
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

        String[] result = matchClient.getListOfMatchIdsByPuuid(route, puuid, start, count);

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

    @Test
    void match_shouldReturnMatchDto() {
        String matchId = "EUW1_1234567890";
        Regions.RegionalRoute route = Regions.RegionalRoute.EUROPE;

        MatchDto expected = new MatchDto(null, null);

        ApiResponse<MatchDto> response = new ApiResponse<>(200, Map.of(), expected);
        when(riotHttp.get(any(URI.class), eq(MatchDto.class))).thenReturn(response);

        MatchDto result = matchClient.getMatchByMatchId(route, matchId);

        assertThat(result).isNotNull();

        verify(riotHttp).get(
                eq(URI.create("https://europe.api.riotgames.com/lol/match/v5/matches/" + matchId)),
                eq(MatchDto.class)
        );
    }

    @Test
    void matchTimeline_shouldReturnTimelineDto() {
        String matchId = "EUW1_1234567890";
        Regions.RegionalRoute route = Regions.RegionalRoute.EUROPE;

        TimelineDto expected = new TimelineDto(null, null);

        ApiResponse<TimelineDto> response = new ApiResponse<>(200, Map.of(), expected);
        when(riotHttp.get(any(URI.class), eq(TimelineDto.class))).thenReturn(response);

        TimelineDto result = matchClient.getMatchTimelineByMatchId(route, matchId);

        assertThat(result).isNotNull();

        verify(riotHttp).get(
                eq(URI.create("https://europe.api.riotgames.com/lol/match/v5/matches/" + matchId + "/timeline")),
                eq(TimelineDto.class)
        );
    }
}
