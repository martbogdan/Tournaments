package com.task.tournaments.dto;

import com.task.tournaments.model.Match;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchInputDTO {
    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    public static Match of(MatchInputDTO dto) {
        return new Match(dto.getStartTime(), dto.getFinishTime());
    }
}
