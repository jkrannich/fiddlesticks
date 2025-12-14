package wrapper.service;

import core.config.Regions;
import wrapper.domain.RankSnapshot;
import wrapper.domain.RankedQueue;

import java.util.Optional;

public interface RankService {

    Optional<RankSnapshot> rankByRiotId(
            Regions.RegionalRoute route,
            Regions.PlatformRegion platformRegion,
            String gameName,
            String tagLine,
            RankedQueue queue
    );

    default String rankTextByRiotId(
            Regions.RegionalRoute route,
            Regions.PlatformRegion platformRegion,
            String gameName,
            String tagLine,
            RankedQueue queue
    ) {
        return rankByRiotId(route, platformRegion, gameName, tagLine, queue)
                .map(RankSnapshot::formatShort)
                .orElse("Unranked");
    }
}
