package com.task.tournaments.service.impl;

import com.task.tournaments.exceptions.EntityNotFoundException;
import com.task.tournaments.model.Match;
import com.task.tournaments.model.Participant;
import com.task.tournaments.model.Tournament;
import com.task.tournaments.repository.ParticipantRepository;
import com.task.tournaments.repository.TournamentRepository;
import com.task.tournaments.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public ParticipantServiceImpl(ParticipantRepository participantRepository, TournamentRepository tournamentRepository) {
        this.participantRepository = participantRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public List<Participant> getAll() {
        return participantRepository.findAll();
    }

    @Override
    public Participant getById(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participant with id " + id + " not found!"));
    }

    @Override
    public Participant createOrUpdate(Participant participant) {
        if (participant.getId() != null) {
            Optional<Participant> participantOptional = participantRepository.findById(participant.getId());
            if (participantOptional.isPresent()) {
                Participant newParticipant = participantOptional.get();
                newParticipant.setNickname(participant.getNickname());
                return participantRepository.save(newParticipant);
            }
        }
        return participantRepository.save(participant);
    }

    @Override
    public void deleteById(Long id) {
        Participant participantToDelete = participantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Participant with id " + id + " not found!"));
        participantRepository.delete(participantToDelete);
    }

    @Override
    public boolean addParticipantToTournament(Participant participant, Tournament tournament) {
        Participant participantDB = participantRepository.getOne(participant.getId());
        Tournament tournamentDB = tournamentRepository.getOne(tournament.getId());
        boolean isPresent = tournamentDB.getParticipants().stream().anyMatch(p -> p.equals(participantDB));
        if (tournamentDB.getParticipants().size() < tournamentDB.getParticipantsNumber() && !isPresent) {
            tournamentDB.getParticipants().add(participantDB);
            return tournamentRepository.save(tournamentDB) != null;
        }
        return false;
    }

    @Override
    public boolean removeParticipantFromTournament(Participant participant, Tournament tournament) {
        Participant participantDB = participantRepository.getOne(participant.getId());
        Tournament tournamentDB = tournamentRepository.getOne(tournament.getId());
        tournamentDB.getParticipants().remove(participantDB);
        return tournamentRepository.save(tournamentDB) != null;
    }

    @Override
    public List<Participant> getLosers(List<Match> matches) {
        List<Participant> losers = new ArrayList<>();
        for (Match match : matches) {
            if (match.getScore1() > match.getScore2()) {
                losers.add(match.getParticipant2());
            } else if (match.getScore1() < match.getScore2()) {
                losers.add(match.getParticipant1());
            }
        }
        return losers;
    }
}
