package core.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import core.config.Regions;
import core.dto.account.AccountDto;
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
class AccountClientTest {

    @Mock
    private RiotHttp riotHttp;

    private AccountClient accountClient;

    @BeforeEach
    void setUp() {
        accountClient = new AccountClient(riotHttp);
    }

    @Test
    void byRiotId_shouldCallCorrectEndpointAndReturnCorrectAccountDto() {
        String gameName = "Thayger";
        String tagLine = "Soul";
        AccountDto expectedAccount = new AccountDto(
                "11111111",
                gameName,
                tagLine
        );

        ApiResponse<AccountDto> response = new ApiResponse<>(200, Map.of(), expectedAccount);
        when(riotHttp.get(any(URI.class), eq(AccountDto.class))).thenReturn(response);

        AccountDto result = accountClient.byRiotId(Regions.RegionalRoute.EUROPE, gameName, tagLine);

        assertThat(result).isNotNull();
        assertThat(result.puuid()).isEqualTo("11111111");
        assertThat(result.gameName()).isEqualTo(gameName);
        assertThat(result.tagLine()).isEqualTo(tagLine);

        verify(riotHttp).get(
                eq(URI.create("https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/Thayger/Soul")),
                eq(AccountDto.class)
        );
    }

    @Test
    void byPuuid_shouldCallCorrectEndpointAndReturnCorrectAccountDto() {
        String puuid = "test-puuid";
        AccountDto expectedAccount = new AccountDto(
                puuid,
                "Thayger",
                "Soul"
        );

        ApiResponse<AccountDto> response = new ApiResponse<>(200, Map.of(), expectedAccount);
        when(riotHttp.get(any(URI.class), eq(AccountDto.class))).thenReturn(response);

        AccountDto result = accountClient.byPuuid(Regions.RegionalRoute.EUROPE, puuid);

        assertThat(result).isNotNull();
        assertThat(result.puuid()).isEqualTo(puuid);
        assertThat(result.gameName()).isEqualTo("Thayger");
        assertThat(result.tagLine()).isEqualTo("Soul");

        verify(riotHttp).get(
                eq(URI.create("https://europe.api.riotgames.com/riot/account/v1/accounts/by-puuid/" + puuid)),
                eq(AccountDto.class)
        );
    }
}
