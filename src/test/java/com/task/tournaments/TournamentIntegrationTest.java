package com.task.tournaments;

import com.task.tournaments.exceptions.EntityNotFoundException;
import com.task.tournaments.model.Tournament;
import com.task.tournaments.service.TournamentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
public class TournamentIntegrationTest {
    private final TournamentService tournamentService;

    @Autowired
    public TournamentIntegrationTest(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @Test
    @Rollback
    void createTournamentTest() {
        Tournament tournament = new Tournament();
        Tournament actual = tournamentService.createOrUpdate(tournament);

        Assertions.assertEquals(tournament, actual);
    }

    @Test
    @Rollback
    public void updateTournamentTest() {
        Tournament expected = new Tournament();
        expected.setTitle("Tournament_1");
        tournamentService.createOrUpdate(expected);
        expected.setTitle("Tournament_2");
        Tournament actual = tournamentService.createOrUpdate(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void getAllTournamentsTest() {
        Tournament tournament1 = new Tournament();
        tournament1.setTitle("Tournament_1");
        Tournament tournament2 = new Tournament();
        tournament2.setTitle("Tournament_2");
        tournamentService.createOrUpdate(tournament1);
        tournamentService.createOrUpdate(tournament2);
        List<Tournament> expected = Arrays.asList(tournament1, tournament2);
        List<Tournament> actual = tournamentService.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void getTournamentByIdTest() {
        Tournament expected = new Tournament();
        expected.setTitle("Tournament_1");
        tournamentService.createOrUpdate(expected);
        Assertions.assertEquals(expected, tournamentService.getById(expected.getId()));
    }

    @Test
    @Rollback
    void deleteTournamentByIdTest() {
        Tournament expected = new Tournament();
        expected.setTitle("Tournament_1");
        tournamentService.createOrUpdate(expected);

        tournamentService.deleteById(expected.getId());
        Assertions.assertThrows(EntityNotFoundException.class, () -> tournamentService.getById(expected.getId()));
    }
}
