package com.task.tournaments.dto;

import com.task.tournaments.model.Match;
import com.task.tournaments.model.Participant;
import com.task.tournaments.model.Tournament;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentOutputDTO {
    private Long id;
    private String title;
    List<Participant> participants;
    List<Match> matches;

    public static TournamentOutputDTO of(Tournament tournament) {
        return new TournamentOutputDTO(tournament.getId(), tournament.getTitle(), tournament.getParticipants(), tournament.getMatches());
    }
}
