package com.task.tournaments.service.impl;

import com.task.tournaments.exceptions.EntityNotFoundException;
import com.task.tournaments.model.Tournament;
import com.task.tournaments.repository.TournamentRepository;
import com.task.tournaments.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;

    @Autowired
    public TournamentServiceImpl(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

    @Override
    public Tournament getById(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tournament with id " + id + " not found!"));
    }

    @Override
    public Tournament createOrUpdate(Tournament tournament) {
        if (tournament.getId() != null) {
            Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournament.getId());
            if (tournamentOptional.isPresent()) {
                Tournament newTournament = tournamentOptional.get();
                newTournament.setTitle(newTournament.getTitle());
                return tournamentRepository.save(newTournament);
            }
        }
        return tournamentRepository.save(tournament);
    }

    @Override
    public void deleteById(Long id) {
        Optional<Tournament> tournamentToDelete = tournamentRepository.findById(id);
        if (tournamentToDelete.isPresent()) {
            tournamentRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Tournament with id " + id + " not found!");
        }
    }
}
