import core.RiotApi;
import core.config.Regions;
import core.config.RiotApiConfig;
import core.dto.summoner.SummonerDto;
import core.http.JavaNetRiotHttp;
import core.http.RiotHttp;
import core.util.RiotIdResolver;
import wrapper.domain.MatchSummary;
import wrapper.mapping.MatchMapper;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String apiKey = System.getenv("RIOT_API_KEY");
        if (apiKey == null) {
            System.err.println("Please set RIOT_API_KEY environment variable");
            return;
        }

        RiotApiConfig config = RiotApiConfig.of(apiKey, Regions.PlatformRegion.EUW1, Regions.RegionalRoute.EUROPE);
        RiotHttp riotHttp = new JavaNetRiotHttp(config);
        RiotApi riotApi = new RiotApi(riotHttp);

       String puuid = new RiotIdResolver(riotApi.account()).puuidOf(Regions.RegionalRoute.EUROPE, "Thayger", "Soul");

       SummonerDto summoner = riotApi.summoner().byPuuid(Regions.PlatformRegion.EUW1, puuid);
       System.out.println(summoner);

       String[] matchIds = riotApi.match().idsByPuuid(Regions.RegionalRoute.EUROPE, puuid, 0, 3);
       System.out.println("Match ids: " + Arrays.toString(matchIds) + "\n");

        List<MatchSummary> summaries = Arrays.stream(matchIds)
                .map(id -> riotApi.match().match(Regions.RegionalRoute.EUROPE, id))
                .map(dto -> MatchMapper.toSummaryForPuuid(dto, puuid))
                .toList();

        for (MatchSummary summary : summaries) {
            System.out.println(summary);
        }
    }
}
