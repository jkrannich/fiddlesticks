package core.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MatchHistoryQueryTest {

    @Test
    void builderShouldExposeReadableDefaultsAndFilters() {
        final MatchHistoryQuery query = MatchHistoryQuery.builder()
                .start(20)
                .count(10)
                .startTime(Instant.ofEpochSecond(100))
                .endTime(Instant.ofEpochSecond(200))
                .queue(420)
                .type("ranked")
                .build();

        assertThat(query.getStart()).isEqualTo(20);
        assertThat(query.getCount()).isEqualTo(10);
        assertThat(query.queryParameters()).containsExactlyInAnyOrderEntriesOf(
                java.util.Map.of(
                        "start", "20",
                        "count", "10",
                        "startTime", "100",
                        "endTime", "200",
                        "queue", "420",
                        "type", "ranked"
                )
        );
    }

    @Test
    void invalidCountShouldFailAtConstruction() {
        assertThatThrownBy(() -> MatchHistoryQuery.builder().count(101).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("between 1 and 100");
    }
}
