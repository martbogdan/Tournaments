package com.task.tournaments.dto;

import com.task.tournaments.model.Match;
import com.task.tournaments.model.Participant;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchInputDTO {
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private Participant participant1;
    private Participant participant2;
    private Integer score1;
    private Integer score2;

    public static Match of(MatchInputDTO dto) {
        return new Match(
                dto.getStartTime(),
                dto.getFinishTime(),
                dto.getParticipant1(),
                dto.getParticipant2(),
                dto.getScore1(),
                dto.getScore2());
    }
}
