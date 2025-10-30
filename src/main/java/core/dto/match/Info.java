package core.dto.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Info(
        String gameMode,
        String gameType,
        String gameVersion,
        long gameDuration,
        long gameCreation,
        List<Participant> participants,
        List<Team> teams,
        String queueId
) {}
