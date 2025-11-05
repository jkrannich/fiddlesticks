package core;

import core.client.AccountV1Client;
import core.client.ChampionMasteryV4Client;
import core.client.MatchV5Client;
import core.client.SummonerV4Client;
import core.http.RiotHttp;

public final class RiotApi {
    private final AccountV1Client accountV1Client;
    private final MatchV5Client matchV5Client;
    private final SummonerV4Client summonerV4Client;
    private final ChampionMasteryV4Client championMasteryV4Client;

    public RiotApi(RiotHttp riotHttp) {
        this.accountV1Client = new AccountV1Client(riotHttp);
        this.matchV5Client = new MatchV5Client(riotHttp);
        this.summonerV4Client = new SummonerV4Client(riotHttp);
        this.championMasteryV4Client = new ChampionMasteryV4Client(riotHttp);
    }

    public AccountV1Client account() {
        return accountV1Client;
    }

    public SummonerV4Client summoner() {
        return summonerV4Client;
    }

    public MatchV5Client match() {
        return matchV5Client;
    }

    public ChampionMasteryV4Client championMastery() {
        return championMasteryV4Client;
    }
}
