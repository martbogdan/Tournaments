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
    private Participant participant;
    private Integer scores;

    public static MatchOutputDTO of(Match match) {
        return new MatchOutputDTO(match.getId(), match.getStartTime(), match.getFinishTime(), match.getParticipant(), match.getScores());
    }

}
