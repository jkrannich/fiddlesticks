package integration;

import static org.assertj.core.api.Assertions.assertThat;
import core.RiotApi;
import core.config.Regions;
import core.config.RiotApiConfig;
import core.dto.account.AccountDto;
import core.dto.challenges.ChallengeConfigInfoDto;
import core.dto.championMastery.ChampionMasteryDto;
import core.dto.championRotation.ChampionInfoDto;
import core.dto.summoner.SummonerDto;
import core.http.JavaNetRiotHttp;
import core.http.RiotHttp;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RiotApiIntegrationTest {

    private static RiotApi riotApi;
    private static final String TEST_GAME_NAME = "Thayger";
    private static final String TEST_TAG_LINE = "Soul";

    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String apiKey = dotenv.get("RIOT_API_KEY");

        if (apiKey == null) {
            throw new IllegalStateException("Please set RIOT_API_KEY environment variable");
        }

        RiotApiConfig config = RiotApiConfig.of(apiKey, Regions.PlatformRegion.EUW1, Regions.RegionalRoute.EUROPE);

        RiotHttp riotHttp = new JavaNetRiotHttp(config);
        riotApi = new RiotApi(riotHttp);
    }

    @Test
    void shouldGetAccountByRiotId() {
        AccountDto account = riotApi.account().byRiotId(Regions.RegionalRoute.EUROPE, TEST_GAME_NAME, TEST_TAG_LINE);

        assertThat(account).isNotNull();
        assertThat(account.puuid()).isNotBlank();
        assertThat(account.gameName()).isEqualTo(TEST_GAME_NAME);
        assertThat(account.tagLine()).isEqualTo(TEST_TAG_LINE.toUpperCase());
    }

    @Test
    void shouldFetchSummonerByPuuid() {
        AccountDto account = riotApi.account().byRiotId(
                Regions.RegionalRoute.EUROPE,
                TEST_GAME_NAME,
                TEST_TAG_LINE
        );

        SummonerDto summoner = riotApi.summoner().byPuuid(Regions.PlatformRegion.EUW1, account.puuid());

        assertThat(summoner).isNotNull();
        assertThat(summoner.puuid()).isEqualTo(account.puuid());
        assertThat(summoner.summonerLevel()).isGreaterThan(0);
    }

    @Test
    void shouldFetchChampionMasteries() {
        AccountDto account = riotApi.account().byRiotId(
                Regions.RegionalRoute.EUROPE,
                TEST_GAME_NAME,
                TEST_TAG_LINE
        );

        List<ChampionMasteryDto> masteries = riotApi.championMastery().getChampionMasteriesByPuuid(Regions.PlatformRegion.EUW1, account.puuid());

        assertThat(masteries).isNotNull();
        assertThat(masteries.getFirst().championLevel()).isGreaterThan(0);
    }

    @Test
    void shouldFetchMatchHistory() {
        AccountDto account = riotApi.account().byRiotId(
                Regions.RegionalRoute.EUROPE,
                TEST_GAME_NAME,
                TEST_TAG_LINE
        );

        String[] matchIds = riotApi.match().getListOfMatchIdsByPuuid(Regions.RegionalRoute.EUROPE, account.puuid(), 0, 5);

        assertThat(matchIds).isNotEmpty();
        assertThat(matchIds).allMatch(id -> id.contains("_"));
    }

    @Test
    void shouldFetchChampionRotation() {
        ChampionInfoDto championRotation = riotApi.champion().getChampionRotation(Regions.PlatformRegion.EUW1);

        assertThat(championRotation).isNotNull();
        assertThat(championRotation.freeChampionIds()).isNotEmpty();
        assertThat(championRotation.maxNewPlayerLevel()).isGreaterThan(0);
        assertThat(championRotation.freeChampionIdsForNewPlayers()).isNotEmpty();
    }

    @Test
    void shouldFetchChallengeConfigs() {
        List<ChallengeConfigInfoDto> configs = riotApi.challenges().listAllBasicChallengeConfigInfo(Regions.PlatformRegion.EUW1);

        assertThat(configs).isNotNull();
        assertThat(configs).isNotEmpty();
        assertThat(configs.getFirst().id()).isGreaterThanOrEqualTo(0);
    }
}
