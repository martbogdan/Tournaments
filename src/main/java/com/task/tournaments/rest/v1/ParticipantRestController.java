package com.task.tournaments.rest.v1;

import com.task.tournaments.dto.ParticipantInputDTO;
import com.task.tournaments.dto.ParticipantOutputDTO;
import com.task.tournaments.dto.TournamentOutputDTO;
import com.task.tournaments.model.Participant;
import com.task.tournaments.service.ParticipantService;
import com.task.tournaments.service.TournamentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Participant")
@RepositoryRestController
@RequestMapping("/api/v1/participant")
public class ParticipantRestController {
    private final ParticipantService participantService;
    private final TournamentService tournamentService;

    @Autowired
    public ParticipantRestController(ParticipantService participantService, TournamentService tournamentService) {
        this.participantService = participantService;
        this.tournamentService = tournamentService;
    }

    @ApiOperation(value = "Get all Participants", response = ResponseEntity.class)
    @GetMapping("/all")
    public ResponseEntity<List<ParticipantOutputDTO>> getAllParticipants() {
        List<ParticipantOutputDTO> participant = participantService.getAll()
                .stream()
                .map(ParticipantOutputDTO::of)
                .collect(Collectors.toList());
        return new ResponseEntity<>(participant, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Participant by id", response = ResponseEntity.class)
    @GetMapping("/{id}")
    public ResponseEntity<ParticipantOutputDTO> getParticipantById(
            @ApiParam(value = "Participant param id", example = "1")
            @PathVariable Long id) {
        ParticipantOutputDTO participant = ParticipantOutputDTO.of(participantService.getById(id));
        return new ResponseEntity<>(participant, HttpStatus.OK);
    }

    @ApiOperation(value = "Create Participant", response = ResponseEntity.class)
    @PostMapping
    public ResponseEntity<ParticipantOutputDTO> createNewParticipant(
            @ApiParam(value = "Request body Participant ", required = true, name = "Participant input")
            @Validated
            @RequestBody ParticipantInputDTO participantInput) {
        ParticipantOutputDTO participant = ParticipantOutputDTO.of(participantService.createOrUpdate(ParticipantInputDTO.of(participantInput)));
        return new ResponseEntity<>(participant, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update Participant", response = ResponseEntity.class)
    @PutMapping("/edit/{id}")
    public ResponseEntity<ParticipantOutputDTO> updateParticipant(
            @ApiParam(value = "Participant param id", example = "1")
            @PathVariable Long id,
            @ApiParam(value = "Request body Participant ", required = true, name = "Participant input")
            @RequestBody Participant participant) {
        Participant participantDB = participantService.getById(id);
        participantDB.setNickname(participant.getNickname());
        ParticipantOutputDTO outputDTO = ParticipantOutputDTO.of(participantService.createOrUpdate(participantDB));
        return new ResponseEntity<>(outputDTO, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete Participant", response = ResponseEntity.class)
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteParticipant(
            @ApiParam(value = "Participant param id", example = "1")
            @PathVariable Long id) {
        participantService.deleteById(id);
    }

    @ApiOperation(value = "Add Participant in Tournament", response = ResponseEntity.class)
    @GetMapping("/{participant_id}/add/{tournament_id}")
    public ResponseEntity<TournamentOutputDTO> addParticipantToTournament(
            @ApiParam(value = "Participant param id", example = "1")
            @PathVariable Long participant_id,
            @ApiParam(value = "Tournament param id", example = "1")
            @PathVariable Long tournament_id) {
        participantService.addParticipantToTournament(participantService.getById(participant_id), tournamentService.getById(tournament_id));
        TournamentOutputDTO outputDTO = TournamentOutputDTO.of(tournamentService.getById(tournament_id));
        return new ResponseEntity<>(outputDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "Remove Participant from Tournament", response = ResponseEntity.class)
    @GetMapping("/{participant_id}/remove/{tournament_id}")
    public ResponseEntity<TournamentOutputDTO> removeParticipantFromTournament(
            @ApiParam(value = "Participant param id", example = "1")
            @PathVariable Long participant_id,
            @ApiParam(value = "Tournament param id", example = "1")
            @PathVariable Long tournament_id) {
        participantService.removeParticipantFromTournament(participantService.getById(participant_id), tournamentService.getById(tournament_id));
        TournamentOutputDTO outputDTO = TournamentOutputDTO.of(tournamentService.getById(tournament_id));
        return new ResponseEntity<>(outputDTO, HttpStatus.OK);
    }
}
