package com.task.tournaments.dto;

import com.task.tournaments.model.Participant;
import lombok.Data;

@Data
public class ParticipantInputDTO {
    private String nickname;

    public static Participant of(ParticipantInputDTO dto) {
        return new Participant(dto.getNickname());
    }

}
