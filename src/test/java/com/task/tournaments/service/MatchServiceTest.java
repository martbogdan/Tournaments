package com.task.tournaments.service;

import com.task.tournaments.model.Match;
import com.task.tournaments.model.Participant;
import com.task.tournaments.repository.MatchRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MatchServiceTest {
    private final MatchService matchService;

    @Autowired
    public MatchServiceTest(MatchService matchService) {
        this.matchService = matchService;
    }

    @MockBean
    private MatchRepository matchRepository;

    @Test
    void createMatchTest() {
        Match match = new Match();

        Mockito.doReturn(match).when(matchRepository).save(match);
        Match actual = matchService.createOrUpdate(match);

        Assertions.assertEquals(match, actual);
        Mockito.verify(matchRepository, Mockito.times(1)).save(match);
    }

    @Test
    public void updateMatchTest() {
        Long id = 1L;
        Match expected = new Match(
                id,
                LocalDateTime.now(),
                LocalDateTime.now(),
                new Participant("P_1"),
                new Participant("P_2"),
                0, 0);

        Mockito.doReturn(expected).when(matchRepository).save(expected);
        expected.setScore1(5);
        expected.setScore2(3);

        Mockito.doReturn(expected).when(matchRepository).save(expected);
        Match actual = matchService.createOrUpdate(expected);
        Assertions.assertEquals(expected, actual);
        Mockito.verify(matchRepository, Mockito.times(1)).save(expected);
    }

    @Test
    void getAllMatchesTest() {
        Match m1 = new Match(
                LocalDateTime.now(),
                LocalDateTime.now(),
                new Participant("P_1"),
                new Participant("P_2"),
                0, 0);
        Match m2 = new Match(
                LocalDateTime.now(),
                LocalDateTime.now(),
                new Participant("P_3"),
                new Participant("P_4"),
                0, 0);
        List<Match> expected = Arrays.asList(m1, m2);
        Mockito.doReturn(expected).when(matchRepository).findAll();
        List<Match> actual = matchService.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getMatchByIdTest() {
        Long id = 1L;
        Match expected = new Match();
        expected.setId(id);

        Mockito.when(matchRepository.findById(id)).thenReturn(Optional.of(expected));
        Assertions.assertEquals(expected, matchService.getById(id));
    }

    @Test
    void deleteMatchByIdTest() {
        Long id = 1L;
        Match expected = new Match();
        expected.setId(id);

        Mockito.when(matchRepository.findById(id)).thenReturn(Optional.of(expected));
        Mockito.when(matchRepository.existsById(expected.getId())).thenReturn(false);
        matchService.deleteById(id);
        Assertions.assertFalse(matchRepository.existsById(id));
    }

    @Test
    void generateMatchesTest() {
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant("P_1"));
        participants.add(new Participant("P_2"));

        Match match = new Match(LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                participants.get(0),
                participants.get(1), 0, 0);

        List<Match> expected = new ArrayList<>();
        expected.add(match);
        List<Match> actual = new ArrayList<>();

        Mockito.doReturn(actual.add(match)).when(matchRepository).save(match);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void isAllMatchesFinishTrueTest() {
        Match match1 = new Match();
        match1.setFinishTime(LocalDateTime.of(2020, 8, 20, 9, 0));
        Match match2 = new Match();
        match2.setFinishTime(LocalDateTime.of(2020, 8, 20, 9, 0));
        List<Match> matches = Arrays.asList(match1, match2);

        Assertions.assertTrue(matchService.isAllMatchesFinish(matches));
    }

    @Test
    void isAllMatchesFinishFalseTest() {
        Match match1 = new Match();
        match1.setFinishTime(LocalDateTime.of(2021, 8, 20, 9, 0));
        Match match2 = new Match();
        match2.setFinishTime(LocalDateTime.of(2021, 8, 20, 9, 0));
        List<Match> matches = Arrays.asList(match1, match2);

        Assertions.assertFalse(matchService.isAllMatchesFinish(matches));
    }
}
