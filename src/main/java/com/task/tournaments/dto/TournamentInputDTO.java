package com.task.tournaments.dto;

import com.task.tournaments.model.Tournament;
import lombok.Data;

@Data
public class TournamentInputDTO {
    private String title;

    public static Tournament of(TournamentInputDTO dto) {
        return new Tournament(dto.getTitle());
    }
}
