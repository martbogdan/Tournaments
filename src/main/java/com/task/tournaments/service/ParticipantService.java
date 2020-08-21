package com.task.tournaments.service;

import com.task.tournaments.model.Match;
import com.task.tournaments.model.Participant;
import com.task.tournaments.model.Tournament;

import java.util.List;

public interface ParticipantService extends EntityService<Participant> {
    boolean addParticipantToTournament(Participant participant, Tournament tournament);
    boolean removeParticipantFromTournament(Participant participant, Tournament tournament);
    List<Participant> getLosers(List<Match> matches);
}
