package com.task.tournaments.service;

import com.task.tournaments.model.Participant;
import com.task.tournaments.model.Tournament;

public interface ParticipantService extends EntityService<Participant> {
    boolean addParticipantToTournament(Participant participant, Tournament tournament);
    boolean removeParticipantFromTournament(Participant participant, Tournament tournament);
}
