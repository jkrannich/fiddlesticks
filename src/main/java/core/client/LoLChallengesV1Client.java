package core.client;

import core.config.Regions;
import core.dto.challenges.ChallengeConfigInfoDto;
import core.http.RiotHttp;

import java.util.List;

public final class LoLChallengesV1Client {
    private RiotHttp riotHttp;

    public LoLChallengesV1Client(RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public List<ChallengeConfigInfoDto> listAllBasicChallengeConfigInfo(final Regions.PlatformRegion platformRegion) {
        return null;
    }
}
