package com.task.tournaments.dto;

import com.task.tournaments.model.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantOutputDTO {
    private Long id;
    private String nickname;

    public static ParticipantOutputDTO of(Participant participant) {
        return new ParticipantOutputDTO(participant.getId(), participant.getNickname());
    }
}
