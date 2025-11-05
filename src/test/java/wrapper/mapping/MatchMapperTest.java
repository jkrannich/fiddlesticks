package wrapper.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import core.dto.match.Info;
import core.dto.match.MatchDto;
import core.dto.match.Metadata;
import core.dto.match.Participant;
import org.junit.jupiter.api.Test;
import wrapper.domain.MatchSummary;

import java.util.List;

class MatchMapperTest {

    @Test
    void toSummaryForPuuid_shouldMapCorrectly() {
        String puuid = "test-puuid";
        Participant participant = new Participant(
                puuid,
                "Faker",
                "Ahri",
                123,
                2,
                10,
                5,
                true,
                400,
                200,
                0,
                0,
                20000,
                500,
                5000,
                null
        );

        Metadata metadata = new Metadata("2", "EUW1_123456", List.of(puuid));
        Info info = new Info(
                "CLASSIC",
                "RANKED_GAME",
                "14.1.1",
                1800L,
                1234567L,
                List.of(participant),
                List.of(),
                "420"
        );
        MatchDto matchDto = new MatchDto(metadata, info);

        MatchSummary summary = MatchMapper.toSummaryForPuuid(matchDto, puuid);

        assertThat(summary.matchId()).isEqualTo("EUW1_123456");
        assertThat(summary.gameMode()).isEqualTo("CLASSIC");
        assertThat(summary.durationSeconds()).isEqualTo(1800L);
        assertThat(summary.champion()).isEqualTo("Ahri");
        assertThat(summary.kills()).isEqualTo(2);
        assertThat(summary.deaths()).isEqualTo(10);
        assertThat(summary.assists()).isEqualTo(5);
        assertThat(summary.win()).isTrue();
    }

    @Test
    void toSummaryForPuuid_shouldThrowWhenPuuidNotFound() {
        String puuid = "test-puuid";
        String wrongPuuid = "wrong-puuid";
        Participant participant = new Participant(
                wrongPuuid,
                "Faker",
                "Ahri",
                10,
                10,
                2,
                1,
                false,
                100,
                100,
                0,
                0,
                10000,
                500,
                5000,
                null
        );

        Metadata metadata = new Metadata("2", "EUW1_123456", List.of(puuid));
        Info info = new Info(
                "CLASSIC",
                "RANKED_GAME",
                "14.1.1",
                1800L,
                1234567L,
                List.of(participant),
                List.of(),
                "420"
        );
        MatchDto matchDto = new MatchDto(metadata, info);

        assertThatThrownBy( () -> MatchMapper.toSummaryForPuuid(matchDto, puuid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No participant found for puuid: " + puuid);
    }
}
