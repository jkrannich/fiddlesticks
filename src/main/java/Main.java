import core.client.SummonerV4Client;
import core.config.Regions;
import core.config.RiotApiConfig;
import core.dto.summoner.SummonerDto;
import core.http.JavaNetRiotHttp;
import core.http.RiotHttp;

public class Main {
    public static void main(String[] args) {
        String apiKey = System.getenv("RIOT_API_KEY");
        if (apiKey == null) {
            System.err.println("Please set RIOT_API_KEY environment variable");
            return;
        }

        RiotApiConfig config = RiotApiConfig.of(apiKey, Regions.PlatformRegion.EUW1, Regions.RegionalRoute.EUROPE);
        RiotHttp riotHttp = new JavaNetRiotHttp(config);

        SummonerV4Client summonerV4Client = new SummonerV4Client(riotHttp);

        try {
            SummonerDto summonerDto = summonerV4Client.byName("Faker", Regions.PlatformRegion.EUW1);
            System.out.println(summonerDto.name());
            System.out.println(summonerDto.summonerLevel());
            System.out.println(summonerDto.accountId());
        } catch (Exception e) {
            System.err.println("Error calling Riot API" + e.getMessage());
            e.printStackTrace();
        }
    }
}
