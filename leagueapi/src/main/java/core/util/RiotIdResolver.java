package core.util;

import core.client.AccountV1Client;
import core.config.Regions;

public final class RiotIdResolver {
    private final AccountV1Client accountV1Client;

    public RiotIdResolver(final AccountV1Client accountV1Client) {
        this.accountV1Client = accountV1Client;
    }

    public String puuidOf(final Regions.RegionalRoute route, final String gameName, final String tagLine) {
        return accountV1Client.byRiotId(route, gameName, tagLine).puuid();
    }
}
