package com.task.tournaments.rest.v1;

import com.task.tournaments.dto.TournamentInputDTO;
import com.task.tournaments.dto.TournamentOutputDTO;
import com.task.tournaments.model.Tournament;
import com.task.tournaments.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RepositoryRestController
@RequestMapping("/api/v1/tournament")
public class TournamentRestController {
    private final TournamentService tournamentService;

    @Autowired
    public TournamentRestController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TournamentOutputDTO>> getAllTournaments() {
        List<TournamentOutputDTO> tournaments = tournamentService.getAll()
                .stream()
                .map(TournamentOutputDTO::of)
                .collect(Collectors.toList());
        return new ResponseEntity<>(tournaments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TournamentOutputDTO> getTournamentById(@PathVariable Long id) {
        TournamentOutputDTO tournament = TournamentOutputDTO.of(tournamentService.getById(id));
        return new ResponseEntity<>(tournament, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TournamentOutputDTO> createNewTournament(@Validated @RequestBody TournamentInputDTO tournamentInput) {
        TournamentOutputDTO tournament = TournamentOutputDTO.of(tournamentService.createOrUpdate(TournamentInputDTO.of(tournamentInput)));
        return new ResponseEntity<>(tournament, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<TournamentOutputDTO> updateTournament(@PathVariable Long id, @RequestBody Tournament tournament) {
        Tournament tournamentDB = tournamentService.getById(id);
        tournamentDB.setTitle(tournament.getTitle());
        tournamentDB.setParticipants(tournament.getParticipants());
        tournamentDB.setMatches(tournament.getMatches());
        TournamentOutputDTO outputDTO = TournamentOutputDTO.of(tournamentService.createOrUpdate(tournamentDB));
        return new ResponseEntity<>(outputDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTournament(@PathVariable Long id) {
        tournamentService.deleteById(id);
    }
}