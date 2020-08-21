package com.task.tournaments.dto;

import com.task.tournaments.model.Match;
import com.task.tournaments.model.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchOutputDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private Participant participant1;
    private Participant participant2;
    private Integer score1;
    private Integer score2;

    public static MatchOutputDTO of(Match match) {
        return new MatchOutputDTO(
                match.getId(),
                match.getStartTime(),
                match.getFinishTime(),
                match.getParticipant1(),
                match.getParticipant2(),
                match.getScore1(),
                match.getScore2());
    }

}
