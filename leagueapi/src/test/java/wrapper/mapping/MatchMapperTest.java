package wrapper.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import core.dto.matchV5.Info;
import core.dto.matchV5.MatchDto;
import core.dto.matchV5.Metadata;
import core.dto.matchV5.Participant;
import org.junit.jupiter.api.Test;
import wrapper.domain.MatchParticipant;
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
        assertThat(summary.teammates()).isEmpty();
        assertThat(summary.opponents()).isEmpty();
    }

    @Test
    void toSummaryForPuuid_shouldClassifyParticipantsByTeamIdNotListOrder() {
        String puuid = "player";
        Participant opponent = participant("opponent", "Enemy", 200, "SOLO", "TOP", 99);
        Participant teammate = participant("teammate", "Ally", 100, "DUO_SUPPORT", "UTILITY", 267);
        Participant player = participant(puuid, "Player", 100, "DUO_CARRY", "BOTTOM", 103);
        Participant secondOpponent = participant("opponent-2", "Enemy 2", 200, "NONE", "JUNGLE", 64);

        MatchDto matchDto = matchWithParticipants(List.of(opponent, teammate, player, secondOpponent));

        MatchSummary summary = MatchMapper.toSummaryForPuuid(matchDto, puuid);

        assertThat(summary.teammates())
                .extracting(MatchParticipant::puuid)
                .containsExactly("teammate");
        assertThat(summary.opponents())
                .extracting(MatchParticipant::puuid)
                .containsExactly("opponent", "opponent-2");
        assertThat(summary.teammates().getFirst())
                .extracting(MatchParticipant::championId, MatchParticipant::role, MatchParticipant::teamPosition)
                .containsExactly(267, "DUO_SUPPORT", "UTILITY");
    }

    @Test
    void toSummaryForPuuid_shouldNotGuessTeamsWhenTeamIdIsUnavailable() {
        String puuid = "player";
        MatchDto matchDto = matchWithParticipants(List.of(
                participant("opponent", "Enemy", 0, "SOLO", "TOP", 99),
                participant(puuid, "Player", 0, "SOLO", "MIDDLE", 103),
                participant("teammate", "Ally", 0, "DUO_SUPPORT", "UTILITY", 267)
        ));

        MatchSummary summary = MatchMapper.toSummaryForPuuid(matchDto, puuid);

        assertThat(summary.teammates()).isEmpty();
        assertThat(summary.opponents()).isEmpty();
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

    private static Participant participant(
            String puuid,
            String summonerName,
            int teamId,
            String role,
            String teamPosition,
            int championId
    ) {
        return new Participant(
                puuid,
                summonerName,
                "Champion " + championId,
                10,
                1,
                2,
                3,
                teamId == 100,
                100,
                20,
                2,
                10,
                10000,
                20000,
                15000,
                null,
                teamId,
                role,
                teamPosition,
                championId
        );
    }

    private static MatchDto matchWithParticipants(List<Participant> participants) {
        Metadata metadata = new Metadata("2", "EUW1_123456", participants.stream()
                .map(Participant::puuid)
                .toList());
        Info info = new Info(
                "CLASSIC",
                "RANKED_GAME",
                "14.1.1",
                1800L,
                1234567L,
                participants,
                List.of(),
                "420"
        );
        return new MatchDto(metadata, info);
    }
}
