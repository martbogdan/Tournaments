package com.task.tournaments;

import com.task.tournaments.exceptions.EntityNotFoundException;
import com.task.tournaments.model.Match;
import com.task.tournaments.model.Participant;
import com.task.tournaments.service.MatchService;
import com.task.tournaments.service.ParticipantService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
public class MatchIntegrationTest {
    private final MatchService matchService;
    private final ParticipantService participantService;

    @Autowired
    public MatchIntegrationTest(MatchService matchService, ParticipantService participantService) {
        this.matchService = matchService;
        this.participantService = participantService;
    }

    @Test
    @Rollback
    void createMatchTest() {
        Match expected = new Match();
        Match actual = matchService.createOrUpdate(expected);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    public void updateMatchTest() {
        Match expected = new Match(
                LocalDateTime.now(),
                LocalDateTime.now(),
                new Participant("P_1"),
                new Participant("P_2"),
                0, 0);
        matchService.createOrUpdate(expected);
        expected.setScore1(5);
        expected.setScore2(3);
        Match actual = matchService.createOrUpdate(expected);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void getAllMatchesTest() {
        Participant p1 = participantService.createOrUpdate(new Participant("P_1"));
        Participant p2 = participantService.createOrUpdate(new Participant("P_2"));
        Participant p3 = participantService.createOrUpdate(new Participant("P_3"));
        Participant p4 = participantService.createOrUpdate(new Participant("P_4"));
        Match m1 = new Match(null, null, p1, p2, 0, 0);
        Match m2 = new Match(null, null, p3, p4, 0, 0);
        matchService.createOrUpdate(m1);
        matchService.createOrUpdate(m2);
        List<Match> expected = Arrays.asList(m1, m2);
        List<Match> actual = matchService.getAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void getMatchByIdTest() {
        Match expected = matchService.createOrUpdate(new Match());

        Assertions.assertEquals(expected, matchService.getById(expected.getId()));
    }

    @Test
    @Rollback
    void deleteMatchByIdTest() {
        Match expected = matchService.createOrUpdate(new Match());
        matchService.deleteById(expected.getId());

        Assertions.assertThrows(EntityNotFoundException.class, () -> participantService.getById(expected.getId()));
    }

    @Test
    @Rollback
    void generateMatchesTest() {
        List<Participant> participants = new ArrayList<>();
        participants.add(participantService.createOrUpdate(new Participant("P_1")));
        participants.add(participantService.createOrUpdate(new Participant("P_2")));

        Match match = new Match(LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                participants.get(0),
                participants.get(1), 0, 0);
        List<Match> expected = new ArrayList<>();
        expected.add(match);

        List<Match> actual = matchService.generateMatches(participants);
        List<Participant> actualParticipants = Arrays.asList(actual.get(0).getParticipant1(), actual.get(0).getParticipant2());

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(participants, actualParticipants);
        Assertions.assertEquals(expected.get(0).getScore1(), actual.get(0).getScore1());
        Assertions.assertEquals(expected.get(0).getScore2(), actual.get(0).getScore2());
    }

    @Test
    @Rollback
    void isAllMatchesFinishTrueTest() {
        Match match1 = new Match();
        match1.setFinishTime(LocalDateTime.of(2020, 8, 20, 9, 0));
        Match match2 = new Match();
        match2.setFinishTime(LocalDateTime.of(2020, 8, 20, 9, 0));
        matchService.createOrUpdate(match1);
        matchService.createOrUpdate(match2);
        List<Match> matches = matchService.getAll();

        Assertions.assertTrue(matchService.isAllMatchesFinish(matches));
    }

    @Test
    @Rollback
    void isAllMatchesFinishFalseTest() {
        Match match1 = new Match();
        match1.setFinishTime(LocalDateTime.of(2021, 8, 20, 9, 0));
        Match match2 = new Match();
        match2.setFinishTime(LocalDateTime.of(2021, 8, 20, 9, 0));
        matchService.createOrUpdate(match1);
        matchService.createOrUpdate(match2);
        List<Match> matches = matchService.getAll();

        Assertions.assertFalse(matchService.isAllMatchesFinish(matches));
    }
}
