package core.client;

import core.config.Regions;
import core.dto.challenges.ApexPlayerInfoDto;
import core.dto.challenges.ChallengeConfigInfoDto;
import core.enums.Level;
import core.http.RiotHttp;

import java.net.URI;
import java.util.List;
import java.util.Map;

public final class LoLChallengesV1Client {
    private RiotHttp riotHttp;

    public LoLChallengesV1Client(RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public List<ChallengeConfigInfoDto> listAllBasicChallengeConfigInfo(final Regions.PlatformRegion platformRegion) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/challenges/v1/challenges/config");
        return List.of(riotHttp.get(uri, ChallengeConfigInfoDto[].class).body());
    }

    public Map<Long, Map<Integer, Map<Level, Double>>> getMapOfLevelToPercentileOfPlayersWhoAchievedIt(final Regions.PlatformRegion platformRegion) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/challenges/v1/challenges/percentiles");
        return riotHttp.get(uri, Map.class).body();
    }

    public ChallengeConfigInfoDto getChallengeConfig(final Regions.PlatformRegion platformRegion, final int challengeId) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/challenges/v1/challenges/" + challengeId + "/config");
        return riotHttp.get(uri, ChallengeConfigInfoDto.class).body();
    }

    public List<ApexPlayerInfoDto> getTopPlayersForEachlevel(final Regions.PlatformRegion platformRegion, final Level level, final int challengeId) {
        URI uri = URI.create(platformRegion.baseUrl() + "/lol/challenges/v1/challenges/" + challengeId + "/leaderboards/by-level/" + level.name());
        return List.of(riotHttp.get(uri, ApexPlayerInfoDto[].class).body());
    }
}
