package core.util;

import core.client.AccountClient;
import core.config.Regions;

public final class RiotIdResolver {
    private final AccountClient accountClient;

    public RiotIdResolver(final AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    public String puuidOf(final Regions.RegionalRoute route, final String gameName, final String tagLine) {
        return accountClient.byRiotId(route, gameName, tagLine).puuid();
    }
}
