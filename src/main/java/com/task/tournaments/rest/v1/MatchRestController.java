package com.task.tournaments.rest.v1;

import com.task.tournaments.dto.MatchInputDTO;
import com.task.tournaments.dto.MatchOutputDTO;
import com.task.tournaments.model.Match;
import com.task.tournaments.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RepositoryRestController
@RequestMapping("/api/v1/match")
public class MatchRestController {
    private final MatchService matchService;

    @Autowired
    public MatchRestController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<MatchOutputDTO>> getAllMatches() {
        List<MatchOutputDTO> match = matchService.getAll()
                .stream()
                .map(MatchOutputDTO::of)
                .collect(Collectors.toList());
        return new ResponseEntity<>(match, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchOutputDTO> getMatchById(@PathVariable Long id) {
        MatchOutputDTO match = MatchOutputDTO.of(matchService.getById(id));
        return new ResponseEntity<>(match, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MatchOutputDTO> createNewMatch(@Validated @RequestBody MatchInputDTO matchInput) {
        MatchOutputDTO match = MatchOutputDTO.of(matchService.createOrUpdate(MatchInputDTO.of(matchInput)));
        return new ResponseEntity<>(match, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<MatchOutputDTO> updateMatch(@PathVariable Long id, @RequestBody Match match) {
        Match matchDB = matchService.getById(id);
        matchDB.setStartTime(match.getStartTime());
        matchDB.setFinishTime(match.getFinishTime());
        matchDB.setScore1(match.getScore1());
        matchDB.setScore2(match.getScore2());
        MatchOutputDTO outputDTO = MatchOutputDTO.of(matchService.createOrUpdate(matchDB));
        return new ResponseEntity<>(outputDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMatch(@PathVariable Long id) {
        matchService.deleteById(id);
    }
}
