package com.task.tournaments.service.impl;

import com.task.tournaments.exceptions.EntityNotFoundException;
import com.task.tournaments.model.Match;
import com.task.tournaments.repository.MatchRepository;
import com.task.tournaments.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {
    private final MatchRepository matchRepository;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public List<Match> getAll() {
        return matchRepository.findAll();
    }

    @Override
    public Match getById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match with id " + id + " not found!"));
    }

    @Override
    public Match createOrUpdate(Match match) {
        if (match.getId() != null) {
            Optional<Match> matchOptional = matchRepository.findById(match.getId());
            if (matchOptional.isPresent()) {
                Match newMatch = matchOptional.get();
                newMatch.setStartTime(match.getStartTime());
                newMatch.setFinishTime(match.getFinishTime());
                newMatch.setParticipant(match.getParticipant());
                newMatch.setScores(match.getScores());
                return matchRepository.save(newMatch);
            }
        }
        return matchRepository.save(match);
    }

    @Override
    public void deleteById(Long id) {
        Match matchToDelete = matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match with id " + id + " not found!"));
        matchRepository.delete(matchToDelete);
    }
}
