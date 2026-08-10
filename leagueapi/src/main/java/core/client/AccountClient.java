package core.client;

import core.config.Regions;
import core.dto.account.AccountDto;
import core.http.RiotHttp;
import core.http.RiotUriBuilder;
import core.model.Puuid;
import core.model.RiotId;

import java.net.URI;

/** Thin Account-V1 endpoint client. Use {@link #byRiotId(RiotId)} in a route-scoped client. */
public final class AccountClient {
    private final RiotHttp riotHttp;
    private final Regions.RegionalRoute defaultRoute;

    public AccountClient(final RiotHttp riotHttp) {
        this(riotHttp, null);
    }

    public AccountClient(final RiotHttp riotHttp, final Regions.RegionalRoute defaultRoute) {
        this.riotHttp = riotHttp;
        this.defaultRoute = defaultRoute;
    }

    public AccountDto byRiotId(final RiotId riotId) {
        return byRiotId(requireDefaultRoute(), riotId);
    }

    public AccountDto byRiotId(final String gameName, final String tagLine) {
        return byRiotId(requireDefaultRoute(), RiotId.of(gameName, tagLine));
    }

    public AccountDto byRiotId(final Regions.RegionalRoute route, final String gameName, final String tagLine) {
        return byRiotId(route, RiotId.of(gameName, tagLine));
    }

    public AccountDto byRiotId(final Regions.RegionalRoute route, final RiotId riotId) {
        final URI uri = RiotUriBuilder.path(
                route.baseUrl(),
                "riot", "account", "v1", "accounts", "by-riot-id",
                riotId.gameName(),
                riotId.tagLine()
        );
        return riotHttp.get(uri, AccountDto.class).body();
    }

    public AccountDto byPuuid(final Puuid puuid) {
        return byPuuid(requireDefaultRoute(), puuid);
    }

    public AccountDto byPuuid(final String puuid) {
        return byPuuid(requireDefaultRoute(), Puuid.of(puuid));
    }

    public AccountDto byPuuid(final Regions.RegionalRoute route, final String puuid) {
        return byPuuid(route, Puuid.of(puuid));
    }

    public AccountDto byPuuid(final Regions.RegionalRoute route, final Puuid puuid) {
        final URI uri = RiotUriBuilder.path(
                route.baseUrl(),
                "riot", "account", "v1", "accounts", "by-puuid", puuid.value()
        );
        return riotHttp.get(uri, AccountDto.class).body();
    }

    private Regions.RegionalRoute requireDefaultRoute() {
        if (defaultRoute == null) {
            throw new IllegalStateException("No default regional route configured; use the route overload or RiotApi.builder()");
        }
        return defaultRoute;
    }
}
