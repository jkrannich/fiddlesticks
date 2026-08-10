package integration;

import static org.assertj.core.api.Assertions.assertThat;
import core.RiotApi;
import core.config.Regions;
import core.dto.account.AccountDto;
import core.dto.challenges.ChallengeConfigInfoDto;
import core.dto.championMastery.ChampionMasteryDto;
import core.dto.championRotation.ChampionInfoDto;
import core.dto.clash.PlayerDto;
import core.dto.summoner.SummonerDto;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;

import java.util.List;

@Tag("integration")
class RiotApiIntegrationTest {

    private static RiotApi riotApi;
    private static final String TEST_GAME_NAME = "Thayger";
    private static final String TEST_TAG_LINE = "Soul";

    @BeforeAll
    static void setUp() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String apiKey = dotenv.get("RIOT_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            apiKey = System.getenv("RIOT_API_KEY");
        }

        Assumptions.assumeTrue(
                apiKey != null && !apiKey.isBlank(),
                "Set RIOT_API_KEY in the environment or .env to run integration tests"
        );

        riotApi = RiotApi.builder()
                .apiKey(apiKey)
                .defaultPlatformRegion(Regions.PlatformRegion.EUW1)
                .defaultRegionalRoute(Regions.RegionalRoute.EUROPE)
                .build();
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
    void shouldFetchClashPlayers() {
        AccountDto account = riotApi.account().byRiotId(
                Regions.RegionalRoute.EUROPE,
                TEST_GAME_NAME,
                TEST_TAG_LINE
        );
        List<PlayerDto> players = riotApi.platform()
                .clash()
                .getPlayersByPuuid(account.puuid());

        assertThat(players).isNotNull();
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
