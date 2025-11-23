package wrapper.mapping;

import core.dto.summoner.SummonerDto;
import wrapper.domain.SummonerProfile;

public final class SummonerMapper {
    private SummonerMapper() {}

    public static SummonerProfile toProfile(final String name, final String tag, final SummonerDto summonerDto) {
        return new SummonerProfile(name, tag, (int) summonerDto.summonerLevel(), summonerDto.profileIconId());
    }
}
