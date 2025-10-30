import core.RiotApi;
import core.client.SummonerV4Client;
import core.config.Regions;
import core.config.RiotApiConfig;
import core.dto.summoner.SummonerDto;
import core.http.JavaNetRiotHttp;
import core.http.RiotHttp;
import core.util.RiotIdResolver;

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
       String[] matchIds = riotApi.match().idsByPuuid(Regions.RegionalRoute.EUROPE, puuid, 0, 10);
       System.out.println(matchIds);
    }
}
