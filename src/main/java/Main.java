import core.RiotApi;
import core.config.Regions;
import core.config.RiotApiConfig;
import core.dto.account.AccountDto;
import core.dto.challenges.ChallengeConfigInfoDto;
import core.dto.challenges.PlayerInfoDto;
import core.dto.championMastery.ChampionMasteryDto;
import core.dto.championRotation.ChampionInfoDto;
import core.dto.clash.PlayerDto;
import core.dto.leagueExp.LeagueEntryDto;
import core.dto.spectator.CurrentGameInfo;
import core.dto.summoner.SummonerDto;
import core.http.JavaNetRiotHttp;
import core.http.RiotHttp;
import core.util.RiotIdResolver;
import io.github.cdimascio.dotenv.Dotenv;
import wrapper.domain.MatchSummary;
import wrapper.mapping.MatchMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("RIOT_API_KEY");

        if (apiKey == null) {
            System.err.println("Please set RIOT_API_KEY environment variable");
            return;
        }

        RiotApiConfig config = RiotApiConfig.of(apiKey, Regions.PlatformRegion.EUW1, Regions.RegionalRoute.EUROPE);
        RiotHttp riotHttp = new JavaNetRiotHttp(config);
        RiotApi riotApi = new RiotApi(riotHttp);

        String puuid = new RiotIdResolver(riotApi.account()).puuidOf(Regions.RegionalRoute.EUROPE, "Thayger", "Soul");
        AccountDto account = riotApi.account().byPuuid(Regions.RegionalRoute.EUROPE, puuid);
        System.out.println(account);


       SummonerDto summoner = riotApi.summoner().byPuuid(Regions.PlatformRegion.EUW1, puuid);
       System.out.println(summoner);

       /*String[] matchIds = riotApi.match().getListOfMatchIdsByPuuid(Regions.RegionalRoute.EUROPE, puuid, 0, 3);
       System.out.println("Match ids: " + Arrays.toString(matchIds) + "\n");

        List<MatchSummary> summaries = Arrays.stream(matchIds)
                .map(id -> riotApi.match().getMatchByMatchId(Regions.RegionalRoute.EUROPE, id))
                .map(dto -> MatchMapper.toSummaryForPuuid(dto, puuid))
                .toList();

        for (MatchSummary summary : summaries) {
            System.out.println(summary);
        }

        List<ChampionMasteryDto> c = riotApi.championMastery().getChampionMasteriesByPuuid(Regions.PlatformRegion.EUW1, puuid);
        System.out.println(c);
        int totalMasteryScore = riotApi.championMastery().getTotalMasteryScore(Regions.PlatformRegion.EUW1, puuid);
        System.out.println(totalMasteryScore);
        ChampionMasteryDto championMasteryDto = riotApi.championMastery().getChampionMasteriesByPuuidAndChampionId(Regions.PlatformRegion.EUW1, puuid, 103);
        System.out.println(championMasteryDto);
        */

        System.out.println("\n=== Champion Rotation ===");
        ChampionInfoDto championRotation = riotApi.champion().getChampionRotation(Regions.PlatformRegion.EUW1);
        System.out.println("Free champions: " + championRotation);

        System.out.println("\n=== League Entries ===");
        Set<LeagueEntryDto> leagueEntries = riotApi.league().getLeagueEntriesInAllQueuesByPuuid(Regions.PlatformRegion.EUW1, puuid);
        System.out.println("League entries: " + leagueEntries);

        System.out.println("\n=== Clash Data ===");
        try {
            List<PlayerDto> clashPlayers = riotApi.clash().getPlayersByPuuid(Regions.PlatformRegion.EUW1, puuid);
            System.out.println("Clash players: " + clashPlayers);
        } catch (Exception e) {
            System.out.println("No clash data (player might not be registered): " + e.getMessage());
        }

        System.out.println("\n=== Challenges ===");
        try {
            PlayerInfoDto playerChallenges = riotApi.challenges().getPlayerInformationWithListOfAllPrgoressedChallenges(Regions.PlatformRegion.EUW1, puuid);
            System.out.println("Player challenges: " + playerChallenges);

            List<ChallengeConfigInfoDto> allChallenges = riotApi.challenges().listAllBasicChallengeConfigInfo(Regions.PlatformRegion.EUW1);
            System.out.println("Total challenges available: " + allChallenges.size());
        } catch (Exception e) {
            System.out.println("Challenges error: " + e.getMessage());
        }

        System.out.println("\n=== Spectator (Live Game) ===");
        try {
            CurrentGameInfo currentGame = riotApi.spectator().getCurrentGameInfoForGivenPuuid(Regions.PlatformRegion.EUW1, puuid);
            System.out.println("Current game: " + currentGame);
            System.out.println("Game mode: " + currentGame.gameMode());
            System.out.println("Participants: " + currentGame.participants().size());
        } catch (Exception e) {
            System.out.println("Not currently in game: " + e.getMessage());
        }
    }
}
