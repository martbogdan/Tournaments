package com.task.tournaments.dto;

import com.task.tournaments.model.Match;
import com.task.tournaments.model.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchOutputDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    public static MatchOutputDTO of(Match match) {
        return new MatchOutputDTO(match.getId(), match.getStartTime(), match.getFinishTime());
    }

}
