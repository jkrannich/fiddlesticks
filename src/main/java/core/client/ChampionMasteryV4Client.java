package core.client;

import core.config.Regions;
import core.dto.championMastery.ChampionMasteryDto;
import core.http.RiotHttp;

import java.net.URI;
import java.util.List;

public final class ChampionMasteryV4Client {
    private final RiotHttp riotHttp;

    public ChampionMasteryV4Client(RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public List<ChampionMasteryDto> getChampionMasteriesByPuuid(Regions.PlatformRegion server, String puuid) {
        URI uri = URI.create(server.baseUrl() + "/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid);
        return List.of(riotHttp.get(uri, ChampionMasteryDto[].class).body());
    }

    public ChampionMasteryDto getChampionMasteriesByPuuidAndChampionId(Regions.PlatformRegion server, String puuid, int championId) {
        URI uri = URI.create(server.baseUrl() + "/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid + "/by-champion/" + championId);
        return riotHttp.get(uri, ChampionMasteryDto.class).body();
    }

    public List<ChampionMasteryDto> getChampionMasteriesByPuuidTop(Regions.PlatformRegion server, String puuid) {
        URI uri = URI.create(server.baseUrl() + "/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid + "/top");
        return List.of(riotHttp.get(uri, ChampionMasteryDto[].class).body());
    }

    public int getTotalMasteryScore(Regions.PlatformRegion server, String puuid) {
        URI uri = URI.create(server.baseUrl() + "/lol/champion-mastery/v4/scores/by-puuid/" + puuid);
        return riotHttp.get(uri, Integer.class).body();
    }
}
