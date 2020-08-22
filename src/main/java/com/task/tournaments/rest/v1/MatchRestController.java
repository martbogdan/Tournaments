package com.task.tournaments.rest.v1;

import com.task.tournaments.dto.MatchInputDTO;
import com.task.tournaments.dto.MatchOutputDTO;
import com.task.tournaments.model.Match;
import com.task.tournaments.service.MatchService;
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

@Api(value = "Match")
@RepositoryRestController
@RequestMapping("/api/v1/match")
public class MatchRestController {
    private final MatchService matchService;

    @Autowired
    public MatchRestController(MatchService matchService) {
        this.matchService = matchService;
    }

    @ApiOperation(value = "Get all Matches", response = ResponseEntity.class)
    @GetMapping("/all")
    public ResponseEntity<List<MatchOutputDTO>> getAllMatches() {
        List<MatchOutputDTO> match = matchService.getAll()
                .stream()
                .map(MatchOutputDTO::of)
                .collect(Collectors.toList());
        return new ResponseEntity<>(match, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Match by id", response = ResponseEntity.class)
    @GetMapping("/{id}")
    public ResponseEntity<MatchOutputDTO> getMatchById(
            @ApiParam(value = "Match param id", example = "1")
            @PathVariable Long id) {
        MatchOutputDTO match = MatchOutputDTO.of(matchService.getById(id));
        return new ResponseEntity<>(match, HttpStatus.OK);
    }

    @ApiOperation(value = "Create Match", response = ResponseEntity.class)
    @PostMapping
    public ResponseEntity<MatchOutputDTO> createNewMatch(
            @ApiParam(value = "Request body Match", required = true, name = "Match input")
            @Validated @RequestBody MatchInputDTO matchInput) {
        MatchOutputDTO match = MatchOutputDTO.of(matchService.createOrUpdate(MatchInputDTO.of(matchInput)));
        return new ResponseEntity<>(match, HttpStatus.CREATED);
    }

    /**
     * Method updateMatch(@PathVariable Long id, @RequestBody Match match) can be used for Summarizing results in match
     */
    @ApiOperation(value = "Update/('Summarize results') Match by id", response = ResponseEntity.class)
    @PutMapping("/edit/{id}")
    public ResponseEntity<MatchOutputDTO> updateMatch(
            @ApiParam(value = "Match param id", example = "1")
            @PathVariable Long id,
            @ApiParam(value = "Request body Match", required = true, name = "Match input")
            @RequestBody Match match) {
        Match matchDB = matchService.getById(id);
        matchDB.setStartTime(match.getStartTime());
        matchDB.setFinishTime(match.getFinishTime());
        matchDB.setScore1(match.getScore1());
        matchDB.setScore2(match.getScore2());
        MatchOutputDTO outputDTO = MatchOutputDTO.of(matchService.createOrUpdate(matchDB));
        return new ResponseEntity<>(outputDTO, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete Match by id", response = ResponseEntity.class)
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMatch(
            @ApiParam(value = "Match param id", example = "1")
            @PathVariable Long id) {
        matchService.deleteById(id);
    }
}
