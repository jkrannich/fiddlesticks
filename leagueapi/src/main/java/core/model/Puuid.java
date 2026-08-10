package core.model;

import java.util.Objects;

/** A globally unique Riot account identifier. */
public record Puuid(String value) {

    public Puuid {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PUUID must not be blank");
        }
    }

    public static Puuid of(final String value) {
        return new Puuid(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
