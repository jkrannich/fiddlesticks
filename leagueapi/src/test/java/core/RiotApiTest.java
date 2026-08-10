package core;

import core.config.Regions;
import core.dto.account.AccountDto;
import core.http.ApiResponse;
import core.http.RiotHttp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RiotApiTest {

    @Mock
    private RiotHttp riotHttp;

    @Test
    void builderAndRegionalScopeShouldHideRepeatedRouteArguments() {
        final RiotApi api = RiotApi.builder()
                .apiKey("test-key")
                .defaultRegionalRoute(Regions.RegionalRoute.EUROPE)
                .build();

        assertThat(api.regional()).isNotNull();
        assertThat(api.platform()).isNotNull();
    }

    @Test
    void injectedTransportCanUseReadableRegionalScope() {
        final RiotApi api = new RiotApi(riotHttp);
        final AccountDto expected = new AccountDto("puuid", "Game Name", "TAG");
        when(riotHttp.get(any(URI.class), eq(AccountDto.class)))
                .thenReturn(new ApiResponse<>(200, Map.of(), expected));

        final AccountDto actual = api.regional(Regions.RegionalRoute.EUROPE)
                .accounts()
                .byRiotId("Game Name", "TAG");

        assertThat(actual).isSameAs(expected);
        verify(riotHttp).get(
                eq(URI.create("https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/Game%20Name/TAG")),
                eq(AccountDto.class)
        );
    }
}
