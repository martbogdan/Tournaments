package com.task.tournaments.service;

import com.task.tournaments.model.Tournament;
import com.task.tournaments.repository.TournamentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TournamentServiceTest {

    private final TournamentService tournamentService;

    @Autowired
    public TournamentServiceTest(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @MockBean
    private TournamentRepository tournamentRepository;

    @Test
    void createTournamentTest() {
        Tournament tournament = new Tournament();
        tournament.setTitle("Tournament 1");
        tournament.setParticipantsNumber(8);
        tournament.setMatchesNumber(0);

        Mockito.doReturn(tournament).when(tournamentRepository).save(tournament);
        Tournament actual = tournamentService.createOrUpdate(tournament);

        Assertions.assertEquals(tournament, actual);
        Mockito.verify(tournamentRepository, Mockito.times(1)).save(tournament);
    }

    @Test
    public void updateTournamentTest() {
        Long id = 1L;
        Tournament expected = new Tournament();
        expected.setId(id);
        expected.setTitle("Tournament_1");

        Mockito.doReturn(expected).when(tournamentRepository).save(expected);
        expected.setTitle("Tournament_2");
        Mockito.doReturn(expected).when(tournamentRepository).save(expected);
        Tournament actual = tournamentService.createOrUpdate(expected);
        Assertions.assertEquals(expected, actual);
        Mockito.verify(tournamentRepository, Mockito.times(1)).save(expected);
    }

    @Test
    void getAllTournamentsTest() {
        Tournament tournament1 = new Tournament();
        tournament1.setTitle("Tournament_1");
        Tournament tournament2 = new Tournament();
        tournament2.setTitle("Tournament_2");
        List<Tournament> expected = Arrays.asList(tournament1, tournament2);
        Mockito.doReturn(expected).when(tournamentRepository).findAll();
        List<Tournament> actual = tournamentService.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getTournamentByIdTest() {
        Long id = 1L;
        Tournament expected = new Tournament();
        expected.setId(id);
        expected.setTitle("Tournament_1");

        Mockito.when(tournamentRepository.findById(id)).thenReturn(Optional.of(expected));
        Assertions.assertEquals(expected, tournamentService.getById(id));
    }

    @Test
    void deleteTournamentByIdTest() {
        Long id = 1L;
        Tournament expected = new Tournament();
        expected.setId(id);
        expected.setTitle("Tournament_1");

        Mockito.when(tournamentRepository.findById(id)).thenReturn(Optional.of(expected));
        Mockito.when(tournamentRepository.existsById(expected.getId())).thenReturn(false);
        tournamentService.deleteById(id);
        Assertions.assertFalse(tournamentRepository.existsById(id));
    }
}
