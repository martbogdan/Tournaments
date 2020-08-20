package com.task.tournaments.service.impl;

import com.task.tournaments.exceptions.EntityNotFoundException;
import com.task.tournaments.model.Participant;
import com.task.tournaments.repository.ParticipantRepository;
import com.task.tournaments.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantServiceImpl(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
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
}
