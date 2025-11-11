package core.dto.leagueV4;

import core.dto.leagueExp.MiniSeriesDto;

public record LeagueItemDto(boolean freshBlood, int wins, MiniSeriesDto miniseries, boolean inactive, boolean veteran, boolean hotStreak, String rank, int leaguePoints, int losses, String puuid) {
}
