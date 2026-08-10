package core.client;

import core.config.Regions;
import core.dto.clash.PlayerDto;
import core.dto.clash.TeamDto;
import core.dto.clash.TournamentDto;
import core.http.RiotHttp;
import core.http.RiotUriBuilder;
import core.model.Puuid;

import java.net.URI;
import java.util.List;

/** Thin, platform-scoped Clash-V1 endpoint client. */
public final class ClashClient {
    private final RiotHttp riotHttp;
    private final Regions.PlatformRegion defaultRegion;

    public ClashClient(final RiotHttp riotHttp) {
        this(riotHttp, null);
    }

    public ClashClient(final RiotHttp riotHttp, final Regions.PlatformRegion defaultRegion) {
        this.riotHttp = riotHttp;
        this.defaultRegion = defaultRegion;
    }

    public List<PlayerDto> getPlayersByPuuid(final Puuid puuid) {
        return getPlayersByPuuid(requireDefaultRegion(), puuid.value());
    }

    public List<PlayerDto> getPlayersByPuuid(final String puuid) {
        return getPlayersByPuuid(requireDefaultRegion(), puuid);
    }

    public List<PlayerDto> getPlayersByPuuid(
            final Regions.PlatformRegion platformRegion,
            final String puuid
    ) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(),
                "lol", "clash", "v1", "players", "by-puuid", puuid
        );
        return List.of(riotHttp.get(uri, PlayerDto[].class).body());
    }

    public TeamDto getTeamById(final String teamId) {
        return getTeamById(requireDefaultRegion(), teamId);
    }

    public TeamDto getTeamById(final Regions.PlatformRegion platformRegion, final String teamId) {
        final URI uri = RiotUriBuilder.path(platformRegion.baseUrl(), "lol", "clash", "v1", "teams", teamId);
        return riotHttp.get(uri, TeamDto.class).body();
    }

    public List<TournamentDto> getAllActiveOrUpcomingTournaments() {
        return getAllActiveOrUpcomingTournaments(requireDefaultRegion());
    }

    public List<TournamentDto> getAllActiveOrUpcomingTournaments(final Regions.PlatformRegion platformRegion) {
        final URI uri = RiotUriBuilder.path(platformRegion.baseUrl(), "lol", "clash", "v1", "tournaments");
        return List.of(riotHttp.get(uri, TournamentDto[].class).body());
    }

    public TournamentDto getTournamentByTeamId(final String teamId) {
        return getTournamentByTeamId(requireDefaultRegion(), teamId);
    }

    public TournamentDto getTournamentByTeamId(final Regions.PlatformRegion platformRegion, final String teamId) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(), "lol", "clash", "v1", "tournaments", "by-team", teamId
        );
        return riotHttp.get(uri, TournamentDto.class).body();
    }

    public TournamentDto getTournamentById(final String tournamentId) {
        return getTournamentById(requireDefaultRegion(), tournamentId);
    }

    public TournamentDto getTournamentById(final Regions.PlatformRegion platformRegion, final String tournamentId) {
        final URI uri = RiotUriBuilder.path(
                platformRegion.baseUrl(), "lol", "clash", "v1", "tournaments", tournamentId
        );
        return riotHttp.get(uri, TournamentDto.class).body();
    }

    private Regions.PlatformRegion requireDefaultRegion() {
        if (defaultRegion == null) {
            throw new IllegalStateException("No default platform region configured; use the region overload or RiotApi.builder()");
        }
        return defaultRegion;
    }
}
