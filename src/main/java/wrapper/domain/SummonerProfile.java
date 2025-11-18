package wrapper.domain;

import core.config.Regions;
import core.enums.Tier;

public record SummonerProfile(
        String name,
        String tag,
        int level,
        int profileIconId
) {
}
