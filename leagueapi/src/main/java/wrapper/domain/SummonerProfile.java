package wrapper.domain;

public record SummonerProfile(
        String name,
        String tag,
        int level,
        int profileIconId
) {
}
