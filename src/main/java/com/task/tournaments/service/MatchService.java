package com.task.tournaments.service;

import com.task.tournaments.model.Match;
import com.task.tournaments.model.Participant;

import java.util.List;

public interface MatchService extends EntityService<Match> {
    List<Match> generateMatches(List<Participant> participants);
}
