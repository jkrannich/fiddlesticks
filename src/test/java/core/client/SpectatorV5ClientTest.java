package core.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import core.config.Regions;
import core.dto.spectator.BannedChampion;
import core.dto.spectator.CurrentGameInfo;
import core.dto.spectator.CurrentGameParticipant;
import core.dto.spectator.GameCustomizationObject;
import core.dto.spectator.Observer;
import core.dto.spectator.Perks;
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
class SpectatorV5ClientTest {

    @Mock
    private RiotHttp riotHttp;

    private SpectatorV5Client spectatorV5Client;

    @BeforeEach
    void setUp() {
        spectatorV5Client = new SpectatorV5Client(riotHttp);
    }

    @Test
    void getCurrentGameInfoForGivenPuuid_shouldCallCorrectEndpointAndReturnCurrentGameInfo() {
        String puuid = "test-puuid-123";
        Regions.PlatformRegion region = Regions.PlatformRegion.EUW1;

        Observer observer = new Observer("encryption-key-123");

        Perks perks = new Perks(
                List.of(8005L, 8008L, 8021L, 9111L, 8014L, 8017L, 5005L, 5008L, 5001L),
                8000L,
                8100L
        );

        GameCustomizationObject customization = new GameCustomizationObject(
                "category1",
                "content1"
        );

        CurrentGameParticipant participant = new CurrentGameParticipant(
                157L,
                perks,
                4901L,
                false,
                100L,
                puuid,
                4L,
                12L,
                List.of(customization)
        );

        BannedChampion bannedChampion = new BannedChampion(
                1,
                84L,
                100L
        );

        CurrentGameInfo expectedGameInfo = new CurrentGameInfo(
                1234567890L,
                "MATCHED_GAME",
                1700000000000L,
                11L,
                180L,
                "EUW1",
                "CLASSIC",
                List.of(bannedChampion),
                420L,
                observer,
                List.of(participant)
        );

        ApiResponse<CurrentGameInfo> response = new ApiResponse<>(200, Map.of(), expectedGameInfo);
        when(riotHttp.get(any(URI.class), eq(CurrentGameInfo.class))).thenReturn(response);

        CurrentGameInfo result = spectatorV5Client.getCurrentGameInfoForGivenPuuid(region, puuid);

        assertThat(result).isNotNull();
        assertThat(result.gameId()).isEqualTo(1234567890L);
        assertThat(result.gameType()).isEqualTo("MATCHED_GAME");
        assertThat(result.gameStartTime()).isEqualTo(1700000000000L);
        assertThat(result.mapId()).isEqualTo(11L);
        assertThat(result.gameLength()).isEqualTo(180L);
        assertThat(result.platformId()).isEqualTo("EUW1");
        assertThat(result.gameMode()).isEqualTo("CLASSIC");
        assertThat(result.gameQueueConfigId()).isEqualTo(420L);
        assertThat(result.observers()).isNotNull();
        assertThat(result.observers().encryptionKey()).isEqualTo("encryption-key-123");
        assertThat(result.bannedChampions()).hasSize(1);
        assertThat(result.bannedChampions().get(0).championId()).isEqualTo(84L);
        assertThat(result.participants()).hasSize(1);
        assertThat(result.participants().get(0).puuid()).isEqualTo(puuid);
        assertThat(result.participants().get(0).championId()).isEqualTo(157L);

        verify(riotHttp).get(
                eq(URI.create("https://euw1.api.riotgames.com/lol/spectator/v5/active-games/by-summoner/" + puuid)),
                eq(CurrentGameInfo.class)
        );
    }
}