package core.client;

import core.config.Regions;
import core.dto.clash.PlayerDto;
import core.dto.clash.TeamDto;
import core.dto.clash.TournamentDto;
import core.http.RiotHttp;

import java.net.URI;
import java.util.List;

public final class ClashV1Client {

    private final RiotHttp riotHttp;

    public ClashV1Client(final RiotHttp riotHttp) {
        this.riotHttp = riotHttp;
    }

    public List<PlayerDto> getPlayersByPuuid(final Regions.PlatformRegion platformRegion, final String puuid) {
        final URI uri = URI.create(platformRegion.baseUrl() + "/lol/clash/v1/players/by-puuid/" + puuid);
        return List.of(riotHttp.get(uri, PlayerDto[].class).body());
    }

    public TeamDto getTeamById(final Regions.PlatformRegion platformRegion, final String teamId) {
        final URI uri = URI.create(platformRegion.baseUrl() + "/lol/clash/v1/teams/" + teamId);
        return riotHttp.get(uri, TeamDto.class).body();
    }

    public List<TournamentDto> getAllActiveOrUpcomingTournaments(final Regions.PlatformRegion platformRegion) {
        final URI uri = URI.create(platformRegion.baseUrl() + "/lol/clash/v1/tournaments");
        return List.of(riotHttp.get(uri, TournamentDto[].class).body());
    }

    public TournamentDto getTournamentByTeamId(final Regions.PlatformRegion platformRegion, final String teamId) {
        final URI uri = URI.create(platformRegion.baseUrl() + "/lol/clash/v1/tournaments/by-team/" + teamId);
        return riotHttp.get(uri, TournamentDto.class).body();
    }

    public TournamentDto getTournamentById(final Regions.PlatformRegion platformRegion, final String tournamentId) {
        final URI uri = URI.create(platformRegion.baseUrl() + "/lol/clash/v1/tournaments/" + tournamentId);
        return riotHttp.get(uri, TournamentDto.class).body();
    }
}
