package core.client;

import core.config.Regions;
import core.dto.account.AccountDto;
import core.http.RiotHttp;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class AccountV1Client {
    private final RiotHttp riotHttp;

    public AccountV1Client(final RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public AccountDto byRiotId(final Regions.RegionalRoute route, final String gameName, final String tagLine) {
        final String g = URLEncoder.encode(gameName, StandardCharsets.UTF_8);
        final String t = URLEncoder.encode(tagLine, StandardCharsets.UTF_8);
        final URI uri = URI.create(route.baseUrl() + "/riot/account/v1/accounts/by-riot-id/" + g + "/" + t);
        return riotHttp.get(uri, AccountDto.class).body();
    }

    public AccountDto byPuuid(final Regions.RegionalRoute route, final String puuid) {
        final URI uri = URI.create(route.baseUrl() + "/riot/account/v1/accounts/by-puuid/" + puuid);
        return riotHttp.get(uri, AccountDto.class).body();
    }
}
