package wrapper.domain;

public record RankSnapshot(
    RankedQueue queue,
    String tier,
    String rank,
    int leaguePoints,
    int wins,
    int losses
) {
    public String formatShort() {
        // Output should be smth like: "Diamond IV 85LP"
        String prettyTier = tier.charAt(0) + tier.substring(1).toLowerCase();
        return prettyTier + " " + rank + " " + leaguePoints + " LP";
    }
}
