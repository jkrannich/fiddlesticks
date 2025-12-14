package wrapper.service;

import core.RiotApi;

public final class DefaultRankService implements RankService {

    private final RiotApi riotApi;

    public DefaultRankService(final RiotApi riotApi) {
        this.riotApi = riotApi;
    }
}
