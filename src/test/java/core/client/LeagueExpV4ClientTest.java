package core.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import core.config.Regions;
import core.dto.leagueExp.LeagueEntryDto;
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
import java.util.Map;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class LeagueExpV4ClientTest {

    @Mock
    private RiotHttp riotHttp;

    private LeagueExpV4Client leagueExpV4Client;

    @BeforeEach
    void setUp() {
        leagueExpV4Client = new LeagueExpV4Client(riotHttp);
    }

    @Test
    void getAllLeagueEntries_shouldReturnSetOfLeagueEntryDto() {
        Regions.PlatformRegion platform = Regions.PlatformRegion.EUW1;
        int page = 1;
        Queue queueType = Queue.RANKED_SOLO_5x5;
        Tier tier = Tier.DIAMOND;
        Division division = Division.II;

        LeagueEntryDto[] entries = new LeagueEntryDto[]{
                new LeagueEntryDto(
                        "league-1", "summoner-1", "puuid-1", "RANKED_SOLO_5x5",
                        "DIAMOND", "II", 75, 100, 95, true, false, false, false, null
                ),
                new LeagueEntryDto(
                        "league-2", "summoner-2", "puuid-2", "RANKED_SOLO_5x5",
                        "DIAMOND", "II", 50, 80, 75, false, true, false, false, null
                )
        };

        ApiResponse<LeagueEntryDto[]> response = new ApiResponse<>(200, Map.of(), entries);
        when(riotHttp.get(any(URI.class), eq(LeagueEntryDto[].class))).thenReturn(response);

        Set<LeagueEntryDto> result = leagueExpV4Client.getAllLeagueEntries(platform, page, queueType, tier, division);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(LeagueEntryDto::summonerId)
                .containsExactlyInAnyOrder("summoner-1", "summoner-2");
        assertThat(result).extracting(LeagueEntryDto::leaguePoints)
                .containsExactlyInAnyOrder(75, 50);
        assertThat(result).extracting(LeagueEntryDto::hotStreak)
                .containsExactlyInAnyOrder(true, false);

        verify(riotHttp).get(
                eq(URI.create(platform.baseUrl() + "/lol/league-exp/v4/entries/"
                        + queueType.name() + "/" + tier.name() + "/" + division.name() + "?page=" + page)),
                eq(LeagueEntryDto[].class)
        );
    }
}
