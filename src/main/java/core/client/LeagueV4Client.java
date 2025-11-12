package core.client;

import core.config.Regions;
import core.dto.leagueExp.LeagueEntryDto;
import core.dto.leagueV4.LeagueListDto;
import core.enums.Division;
import core.enums.Queue;
import core.enums.Tier;
import core.http.RiotHttp;

import java.net.URI;
import java.util.Set;

public final class LeagueV4Client {
    private final RiotHttp riotHttp;

    public LeagueV4Client(RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public LeagueListDto getChallengerLeagueForGivenQueue(Regions.PlatformRegion platformRegion, Queue queueType) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/league/v4/challengerleagues/by-queue/" + queueType.name());
        return riotHttp.get(uri, LeagueListDto.class).body();
    }

    public Set<LeagueEntryDto> getLeagueEntriesInAllQueuesByPuuid(Regions.PlatformRegion platformRegion, String puuid) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/league/v4/entries/by-puuid/" + puuid);
        return Set.of(riotHttp.get(uri, LeagueEntryDto[].class).body());
    }

    public Set<LeagueEntryDto> getAllLeagueEntries(Regions.PlatformRegion platformRegion, Queue queueType, Tier tier, Division division, int page) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/league/v4/entries/" + queueType.name() + "/" + tier.name() + "/" + division.name() + "?page=" + page);
        return Set.of(riotHttp.get(uri, LeagueEntryDto[].class).body());
    }

    public LeagueListDto getGrandMasterLeagueForGivenQueue(Regions.PlatformRegion platformRegion, Queue queueType) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/league/v4/grandmasterleagues/by-queue/" + queueType.name());
        return riotHttp.get(uri, LeagueListDto.class).body();
    }

    public LeagueListDto getLeagueWithGivenIdIncludingInactiveEntries(Regions.PlatformRegion platformRegion, String leagueId) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/league/v4/leagues/" + leagueId);
        return riotHttp.get(uri, LeagueListDto.class).body();
    }

    public LeagueListDto getMasterLeagueForGivenQueue(Regions.PlatformRegion platformRegion, Queue queueType) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/league/v4/masterleagues/by-queue/" + queueType.name());
        return riotHttp.get(uri, LeagueListDto.class).body();
    }
}
