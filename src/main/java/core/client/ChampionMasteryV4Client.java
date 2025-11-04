package core.client;

import core.config.Regions;
import core.dto.championMastery.ChampionMasteryDto;
import core.http.RiotHttp;

import java.util.List;

public final class ChampionMasteryV4Client {
    private final RiotHttp riotHttp;

    public ChampionMasteryV4Client(RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public List<ChampionMasteryDto> getChampionMasteriesByPuuid(Regions.PlatformRegion server, String puuid) {
        return null;
    }
}
