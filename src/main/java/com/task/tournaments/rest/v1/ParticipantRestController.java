package com.task.tournaments.rest.v1;

import com.task.tournaments.dto.ParticipantInputDTO;
import com.task.tournaments.dto.ParticipantOutputDTO;
import com.task.tournaments.model.Participant;
import com.task.tournaments.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RepositoryRestController
@RequestMapping("/api/v1/participant")
public class ParticipantRestController {
    private final ParticipantService participantService;

    @Autowired
    public ParticipantRestController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ParticipantOutputDTO>> getAllParticipants() {
        List<ParticipantOutputDTO> participant = participantService.getAll()
                .stream()
                .map(ParticipantOutputDTO::of)
                .collect(Collectors.toList());
        return new ResponseEntity<>(participant, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantOutputDTO> getParticipantById(@PathVariable Long id) {
        ParticipantOutputDTO participant = ParticipantOutputDTO.of(participantService.getById(id));
        return new ResponseEntity<>(participant, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ParticipantOutputDTO> createNewParticipant(@Validated @RequestBody ParticipantInputDTO participantInput) {
        ParticipantOutputDTO participant = ParticipantOutputDTO.of(participantService.createOrUpdate(ParticipantInputDTO.of(participantInput)));
        return new ResponseEntity<>(participant, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ParticipantOutputDTO> updateParticipant(@PathVariable Long id, @RequestBody Participant participant) {
        Participant participantDB = participantService.getById(id);
        participantDB.setNickname(participant.getNickname());
        ParticipantOutputDTO outputDTO = ParticipantOutputDTO.of(participantService.createOrUpdate(participantDB));
        return new ResponseEntity<>(outputDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteParticipant(@PathVariable Long id) {
        participantService.deleteById(id);
    }
}