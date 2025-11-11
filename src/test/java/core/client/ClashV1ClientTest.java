package core.client;

import core.config.Regions;
import core.dto.clash.PlayerDto;
import core.dto.clash.TeamDto;
import core.dto.clash.TournamentDto;
import core.enums.Position;
import core.enums.Role;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClashV1ClientTest {

    @Mock
    private RiotHttp riotHttp;

    private ClashV1Client clashV1Client;

    @BeforeEach
    void setUp() {
        clashV1Client = new ClashV1Client(riotHttp);
    }

    @Test
    void getPlayersByPuuid_shouldReturnListOfPlayerDto() {
        String puuid = "test-puuid";
        Regions.PlatformRegion  platformRegion = Regions.PlatformRegion.EUN1;

        PlayerDto[] players = new PlayerDto[]{
                new PlayerDto(puuid, "Team 1", Position.BOTTOM, Role.MEMBER),
                new PlayerDto(puuid, "Team 2", Position.MIDDLE, Role.CAPTAIN)
        };

        ApiResponse<PlayerDto[]> response = new ApiResponse<>(200, Map.of(), players);
        when(riotHttp.get(any(URI.class), eq(PlayerDto[].class))).thenReturn(response);

        List<PlayerDto> result = clashV1Client.getPlayersByPuuid(platformRegion, puuid);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).position()).isEqualTo(Position.BOTTOM);
        assertThat(result.get(1).position()).isEqualTo(Position.MIDDLE);
        assertThat(result.get(0).role()).isEqualTo(Role.MEMBER);
        assertThat(result.get(1).role()).isEqualTo(Role.CAPTAIN);
        assertThat(result.get(0).teamId()).isEqualTo("Team 1");
        assertThat(result.get(1).teamId()).isEqualTo("Team 2");

        verify(riotHttp).get(
                eq(URI.create("https://eun1.api.riotgames.com/lol/clash/v1/players/by-puuid/" + puuid)),
                eq(PlayerDto[].class)
        );
    }

    @Test
    void getTeamById_shouldReturnTeamDto() {
        String teamId = "test-teamId";
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.BR1;

        URI expectedUri = URI.create(platformRegion.baseUrl() + "/lol/clash/v1/teams/" + teamId);

        TeamDto teamDto = new TeamDto(
                "1", 1, "Test Team", 1, 1, "Test Captain", "ABC", List.of(
                        new PlayerDto("test-puuid", "Team 1", Position.MIDDLE, Role.MEMBER),
                        new PlayerDto("test-puuid", "Team 1", Position.BOTTOM, Role.MEMBER)
        ));

        ApiResponse<TeamDto> response = new ApiResponse<>(200, Map.of(), teamDto);
        when(riotHttp.get(any(URI.class), eq(TeamDto.class))).thenReturn(response);

        TeamDto result = clashV1Client.getTeamById(platformRegion, teamId);

        assertThat(result).isNotNull();
        assertThat(result).isSameAs(teamDto);

        verify(riotHttp).get(
                eq(expectedUri),
                eq(TeamDto.class)
        );
    }

    @Test
    void getAllActiveOrUpcomingTournaments_shouldReturnListOfTournamentDto() {
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.EUW1;

        TournamentDto[] tournaments = new TournamentDto[]{
                new TournamentDto(1, 101, "clash_tournament_1", "secondary_1", List.of()),
                new TournamentDto(2, 102, "clash_tournament_2", "secondary_2", List.of())
        };

        ApiResponse<TournamentDto[]> response = new ApiResponse<>(200, Map.of(), tournaments);
        when(riotHttp.get(any(URI.class), eq(TournamentDto[].class))).thenReturn(response);

        List<TournamentDto> result = clashV1Client.getAllActiveOrUpcomingTournaments(platformRegion);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo(1);
        assertThat(result.get(0).themeId()).isEqualTo(101);
        assertThat(result.get(0).nameKey()).isEqualTo("clash_tournament_1");
        assertThat(result.get(1).id()).isEqualTo(2);

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/clash/v1/tournaments")),
                eq(TournamentDto[].class)
        );
    }

    @Test
    void getTournamentByTeamId_shouldReturnTournamentDto() {
        String teamId = "test-team-id";
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.NA1;

        TournamentDto tournament = new TournamentDto(
                123, 456, "tournament_key", "secondary_key", List.of()
        );

        ApiResponse<TournamentDto> response = new ApiResponse<>(200, Map.of(), tournament);
        when(riotHttp.get(any(URI.class), eq(TournamentDto.class))).thenReturn(response);

        TournamentDto result = clashV1Client.getTournamentByTeamId(platformRegion, teamId);

        assertThat(result).isNotNull();
        assertThat(result).isSameAs(tournament);
        assertThat(result.id()).isEqualTo(123);
        assertThat(result.themeId()).isEqualTo(456);

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/clash/v1/tournaments/by-team/" + teamId)),
                eq(TournamentDto.class)
        );
    }

    @Test
    void getTournamentById_shouldReturnTournamentDto() {
        String tournamentId = "789";
        Regions.PlatformRegion platformRegion = Regions.PlatformRegion.KR;

        TournamentDto tournament = new TournamentDto(
                789, 999, "kr_tournament", "kr_secondary", List.of()
        );

        ApiResponse<TournamentDto> response = new ApiResponse<>(200, Map.of(), tournament);
        when(riotHttp.get(any(URI.class), eq(TournamentDto.class))).thenReturn(response);

        TournamentDto result = clashV1Client.getTournamentById(platformRegion, tournamentId);

        assertThat(result).isNotNull();
        assertThat(result).isSameAs(tournament);
        assertThat(result.id()).isEqualTo(789);
        assertThat(result.nameKey()).isEqualTo("kr_tournament");

        verify(riotHttp).get(
                eq(URI.create(platformRegion.baseUrl() + "/lol/clash/v1/tournaments/" + tournamentId)),
                eq(TournamentDto.class)
        );
    }
}
