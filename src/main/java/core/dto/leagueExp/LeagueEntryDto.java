package core.dto.leagueExp;

public record LeagueEntryDto(String leagueId, String summonerId, String puuid, String queueType, String tier, String rank, int leaguePoints, int wins, int losses, boolean hotStreak, boolean veteran, boolean freshBlood, boolean inactive, MiniSeriesDto miniseries) {
}
