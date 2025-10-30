package core.client;

import core.config.Regions;
import core.dto.account.AccountDto;
import core.http.RiotHttp;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class AccountV1Client {
    private final RiotHttp riotHttp;

    public AccountV1Client(RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public AccountDto byRiotId(Regions.RegionalRoute route, String gameName, String tagLine) {
        String g = URLEncoder.encode(gameName, StandardCharsets.UTF_8);
        String t = URLEncoder.encode(tagLine, StandardCharsets.UTF_8);
        URI uri = URI.create(route.baseUrl() + "/riot/account/v1/accounts/by-riot-id/" + g + "/" + t);
        return riotHttp.get(uri, AccountDto.class).body();
    }
}
