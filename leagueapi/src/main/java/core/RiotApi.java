package core;

import core.client.AccountV1Client;
import core.client.ChampionMasteryV4Client;
import core.client.ChampionV3Client;
import core.client.ClashV1Client;
import core.client.LeagueExpV4Client;
import core.client.LeagueV4Client;
import core.client.LoLChallengesV1Client;
import core.client.MatchV5Client;
import core.client.SpectatorV5Client;
import core.client.SummonerV4Client;
import core.http.RiotHttp;

public final class RiotApi {
    private final AccountV1Client accountV1Client;
    private final MatchV5Client matchV5Client;
    private final SummonerV4Client summonerV4Client;
    private final ChampionMasteryV4Client championMasteryV4Client;
    private final ChampionV3Client championV3Client;
    private final ClashV1Client clashV1Client;
    private final LeagueV4Client leagueV4Client;
    private final LeagueExpV4Client leagueExpV4Client;
    private final LoLChallengesV1Client loLChallengesV1Client;
    private final SpectatorV5Client spectatorV5Client;

    public RiotApi(final RiotHttp riotHttp) {
        this.accountV1Client = new AccountV1Client(riotHttp);
        this.matchV5Client = new MatchV5Client(riotHttp);
        this.summonerV4Client = new SummonerV4Client(riotHttp);
        this.championMasteryV4Client = new ChampionMasteryV4Client(riotHttp);
        this.championV3Client = new ChampionV3Client(riotHttp);
        this.clashV1Client = new ClashV1Client(riotHttp);
        this.leagueV4Client = new LeagueV4Client(riotHttp);
        this.leagueExpV4Client = new LeagueExpV4Client(riotHttp);
        this.loLChallengesV1Client = new LoLChallengesV1Client(riotHttp);
        this.spectatorV5Client = new SpectatorV5Client(riotHttp);
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

    public ChampionV3Client champion() {
        return championV3Client;
    }

    public ClashV1Client clash() {
        return clashV1Client;
    }

    public LeagueV4Client league() {
        return leagueV4Client;
    }

    public LeagueExpV4Client leagueExp() {
        return leagueExpV4Client;
    }

    public LoLChallengesV1Client challenges() {
        return loLChallengesV1Client;
    }

    public SpectatorV5Client spectator() {
        return spectatorV5Client;
    }
}
