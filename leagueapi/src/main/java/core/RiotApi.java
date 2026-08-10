package core;

import core.client.AccountClient;
import core.client.ChallengesClient;
import core.client.ChampionClient;
import core.client.ChampionMasteryClient;
import core.client.ClashClient;
import core.client.LeagueClient;
import core.client.LeagueExperienceClient;
import core.client.MatchClient;
import core.client.SpectatorClient;
import core.client.StatusClient;
import core.client.SummonerClient;
import core.config.Regions;
import core.config.RiotApiConfig;
import core.http.JavaNetRiotHttp;
import core.http.RiotHttp;
import lombok.Builder;

import java.time.Duration;

/**
 * Entry point for the synchronous Riot API wrapper.
 *
 * <pre>
 * RiotApi api = RiotApi.builder().apiKey(key).build();
 * api.regional().accounts().byRiotId("GameName", "TAG");
 * api.platform().clash().getAllActiveOrUpcomingTournaments();
 * </pre>
 */
public final class RiotApi {
    private final RiotHttp riotHttp;
    private final Regions.PlatformRegion defaultPlatformRegion;
    private final Regions.RegionalRoute defaultRegionalRoute;

    private final AccountClient accountClient;
    private final MatchClient matchClient;
    private final SummonerClient summonerClient;
    private final ChampionMasteryClient championMasteryClient;
    private final ChampionClient championClient;
    private final ClashClient clashClient;
    private final LeagueClient leagueClient;
    private final LeagueExperienceClient leagueExperienceClient;
    private final ChallengesClient challengesClient;
    private final StatusClient statusClient;
    private final SpectatorClient spectatorClient;

    /** Creates a client around a custom transport, useful for tests and advanced integrations. */
    public RiotApi(final RiotHttp riotHttp) {
        this(riotHttp, Regions.PlatformRegion.EUW1, Regions.RegionalRoute.EUROPE);
    }

    public RiotApi(
            final RiotHttp riotHttp,
            final Regions.PlatformRegion defaultPlatformRegion,
            final Regions.RegionalRoute defaultRegionalRoute
    ) {
        this.riotHttp = riotHttp;
        this.defaultPlatformRegion = defaultPlatformRegion;
        this.defaultRegionalRoute = defaultRegionalRoute;
        this.accountClient = new AccountClient(riotHttp, defaultRegionalRoute);
        this.matchClient = new MatchClient(riotHttp, defaultRegionalRoute);
        this.summonerClient = new SummonerClient(riotHttp, defaultPlatformRegion);
        this.championMasteryClient = new ChampionMasteryClient(riotHttp, defaultPlatformRegion);
        this.championClient = new ChampionClient(riotHttp, defaultPlatformRegion);
        this.clashClient = new ClashClient(riotHttp, defaultPlatformRegion);
        this.leagueClient = new LeagueClient(riotHttp, defaultPlatformRegion);
        this.leagueExperienceClient = new LeagueExperienceClient(riotHttp, defaultPlatformRegion);
        this.challengesClient = new ChallengesClient(riotHttp, defaultPlatformRegion);
        this.statusClient = new StatusClient(riotHttp, defaultPlatformRegion);
        this.spectatorClient = new SpectatorClient(riotHttp, defaultPlatformRegion);
    }

    @Builder(builderMethodName = "builder")
    private static RiotApi create(
            final String apiKey,
            final Regions.PlatformRegion defaultPlatformRegion,
            final Regions.RegionalRoute defaultRegionalRoute,
            final Duration timeout
    ) {
        final RiotApiConfig config = RiotApiConfig.builder()
                .apiKey(apiKey)
                .defaultPlatformRegion(defaultPlatformRegion)
                .defaultRegionalRoute(defaultRegionalRoute)
                .timeout(timeout)
                .build();
        return new RiotApi(
                new JavaNetRiotHttp(config),
                config.getDefaultPlatformRegion(),
                config.getDefaultRegionalRoute()
        );
    }

    public AccountClient account() {
        return accountClient;
    }

    public AccountClient accounts() {
        return accountClient;
    }

    public SummonerClient summoner() {
        return summonerClient;
    }

    public MatchClient match() {
        return matchClient;
    }

    public MatchClient matches() {
        return matchClient;
    }

    public ChampionMasteryClient championMastery() {
        return championMasteryClient;
    }

    public ChampionClient champion() {
        return championClient;
    }

    public ClashClient clash() {
        return clashClient;
    }

    public LeagueClient league() {
        return leagueClient;
    }

    public LeagueExperienceClient leagueExp() {
        return leagueExperienceClient;
    }

    public ChallengesClient challenges() {
        return challengesClient;
    }

    public StatusClient status() {
        return statusClient;
    }

    public SpectatorClient spectator() {
        return spectatorClient;
    }

    public RegionalScope regional() {
        return regional(defaultRegionalRoute);
    }

    public RegionalScope regional(final Regions.RegionalRoute route) {
        return new RegionalScope(route);
    }

    public PlatformScope platform() {
        return platform(defaultPlatformRegion);
    }

    public PlatformScope platform(final Regions.PlatformRegion region) {
        return new PlatformScope(region);
    }

    public final class RegionalScope {
        private final Regions.RegionalRoute route;

        private RegionalScope(final Regions.RegionalRoute route) {
            this.route = route;
        }

        public AccountClient accounts() {
            return new AccountClient(riotHttp, route);
        }

        public MatchClient matches() {
            return new MatchClient(riotHttp, route);
        }
    }

    public final class PlatformScope {
        private final Regions.PlatformRegion region;

        private PlatformScope(final Regions.PlatformRegion region) {
            this.region = region;
        }

        public SummonerClient summoners() {
            return new SummonerClient(riotHttp, region);
        }

        public ClashClient clash() {
            return new ClashClient(riotHttp, region);
        }

        public LeagueClient league() {
            return new LeagueClient(riotHttp, region);
        }

        public LeagueExperienceClient leagueExp() {
            return new LeagueExperienceClient(riotHttp, region);
        }

        public ChampionMasteryClient championMastery() {
            return new ChampionMasteryClient(riotHttp, region);
        }

        public ChampionClient champion() {
            return new ChampionClient(riotHttp, region);
        }

        public ChallengesClient challenges() {
            return new ChallengesClient(riotHttp, region);
        }

        public StatusClient status() {
            return new StatusClient(riotHttp, region);
        }

        public SpectatorClient spectator() {
            return new SpectatorClient(riotHttp, region);
        }
    }
}
