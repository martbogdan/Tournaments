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
                newTournament.setTitle(tournament.getTitle());
                newTournament.setParticipantsNumber(tournament.getParticipantsNumber() != null ? tournament.getParticipantsNumber() : newTournament.getParticipantsNumber());
                newTournament.setMatchesNumber(tournament.getMatchesNumber() != null ? tournament.getMatchesNumber() : newTournament.getMatchesNumber());
                newTournament.setParticipants(tournament.getParticipants() != null ? tournament.getParticipants() : newTournament.getParticipants());
                newTournament.setMatches(tournament.getMatches() != null ? tournament.getMatches() : newTournament.getMatches());
                return tournamentRepository.save(newTournament);
            }
        }
        if (tournament.getParticipantsNumber() == null || tournament.getParticipantsNumber() < 8) {
            tournament.setParticipantsNumber(8);
            tournament.setMatchesNumber(0);
        } else {
            if (tournament.getParticipantsNumber() % 8 != 0) {
                int maxNumber = tournament.getParticipantsNumber();
                while (maxNumber % 8 != 0) {
                    maxNumber++;
                }
                tournament.setParticipantsNumber(maxNumber);
                tournament.setMatchesNumber(0);
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
