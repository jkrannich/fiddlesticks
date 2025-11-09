package core.client;

import core.config.Regions;
import core.dto.clash.PlayerDto;
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
}
