package core.model;

/** A player-facing Riot ID consisting of a game name and tag line. */
public record RiotId(String gameName, String tagLine) {

    public RiotId {
        if (gameName == null || gameName.isBlank()) {
            throw new IllegalArgumentException("Riot ID game name must not be blank");
        }
        if (tagLine == null || tagLine.isBlank()) {
            throw new IllegalArgumentException("Riot ID tag line must not be blank");
        }
    }

    public static RiotId of(final String gameName, final String tagLine) {
        return new RiotId(gameName, tagLine);
    }

    @Override
    public String toString() {
        return gameName + "#" + tagLine;
    }
}
