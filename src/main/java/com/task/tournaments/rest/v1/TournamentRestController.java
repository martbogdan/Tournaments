package com.task.tournaments.rest.v1;

import com.task.tournaments.dto.TournamentInputDTO;
import com.task.tournaments.dto.TournamentOutputDTO;
import com.task.tournaments.model.Participant;
import com.task.tournaments.model.Tournament;
import com.task.tournaments.service.MatchService;
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

@Api(value = "Tournament")
@RepositoryRestController
@RequestMapping("/api/v1/tournament")
public class TournamentRestController {
    private final TournamentService tournamentService;
    private final MatchService matchService;
    private final ParticipantService participantService;

    @Autowired
    public TournamentRestController(TournamentService tournamentService, MatchService matchService, ParticipantService participantService) {
        this.tournamentService = tournamentService;
        this.matchService = matchService;
        this.participantService = participantService;
    }

    @ApiOperation(value = "Get all Tournaments", response = ResponseEntity.class)
    @GetMapping("/all")
    public ResponseEntity<List<TournamentOutputDTO>> getAllTournaments() {
        List<TournamentOutputDTO> tournaments = tournamentService.getAll()
                .stream()
                .map(TournamentOutputDTO::of)
                .collect(Collectors.toList());
        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Tournament by id", response = ResponseEntity.class)
    @GetMapping("/{id}")
    public ResponseEntity<TournamentOutputDTO> getTournamentById(
            @ApiParam(value = "Tournament param id", example = "1")
            @PathVariable Long id) {
        TournamentOutputDTO tournament = TournamentOutputDTO.of(tournamentService.getById(id));
        return new ResponseEntity<>(tournament, HttpStatus.OK);
    }

    @ApiOperation(value = "Create Tournament", response = ResponseEntity.class)
    @PostMapping
    public ResponseEntity<TournamentOutputDTO> createNewTournament(
            @ApiParam(value = "Request body Tournament ", required = true, name = "Tournament input")
            @Validated
            @RequestBody TournamentInputDTO tournamentInput) {
        TournamentOutputDTO tournament = TournamentOutputDTO.of(tournamentService.createOrUpdate(TournamentInputDTO.of(tournamentInput)));
        return new ResponseEntity<>(tournament, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update Tournament", response = ResponseEntity.class)
    @PutMapping("/edit/{id}")
    public ResponseEntity<TournamentOutputDTO> updateTournament(
            @ApiParam(value = "Tournament param id", example = "1")
            @PathVariable Long id,
            @ApiParam(value = "Request body Tournament ", required = true, name = "Tournament")
            @RequestBody Tournament tournament) {
        Tournament tournamentDB = tournamentService.getById(id);
        tournamentDB.setTitle(tournament.getTitle());
        tournamentDB.setParticipants(tournament.getParticipants());
        tournamentDB.setMatches(tournament.getMatches());
        TournamentOutputDTO outputDTO = TournamentOutputDTO.of(tournamentService.createOrUpdate(tournamentDB));
        return new ResponseEntity<>(outputDTO, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete Tournament by id", response = ResponseEntity.class)
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTournament(
            @ApiParam(value = "Tournament param id", example = "1") @PathVariable Long id) {
        tournamentService.deleteById(id);
    }

    @ApiOperation(value = "Starting Tournament by id", response = ResponseEntity.class)
    @GetMapping("/start/{id}")
    public ResponseEntity<TournamentOutputDTO> startTournament(
            @ApiParam(value = "Tournament param id", example = "1") @PathVariable Long id) {
        Tournament tournamentDB = tournamentService.getById(id);

        if (tournamentDB.getMatches().size() > 0) {
            if (matchService.isAllMatchesFinish(tournamentDB.getMatches())) {
                List<Participant> losers = participantService.getLosers(tournamentDB.getMatches());
                losers.forEach(participant -> participantService.removeParticipantFromTournament(participant, tournamentDB));
                tournamentDB.setMatches(matchService.generateMatches(tournamentDB.getParticipants()));
                tournamentDB.setMatchesNumber(tournamentDB.getMatchesNumber() + tournamentDB.getMatches().size());
            }
        } else {
            tournamentDB.setMatches(matchService.generateMatches(tournamentDB.getParticipants()));
            tournamentDB.setMatchesNumber(tournamentDB.getMatchesNumber() + tournamentDB.getMatches().size());
        }

        TournamentOutputDTO outputDTO = TournamentOutputDTO.of(tournamentService.createOrUpdate(tournamentDB));
        return new ResponseEntity<>(outputDTO, HttpStatus.CREATED);
    }
}
