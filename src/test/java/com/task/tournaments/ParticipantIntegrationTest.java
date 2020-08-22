package com.task.tournaments;

import com.task.tournaments.exceptions.EntityNotFoundException;
import com.task.tournaments.model.Match;
import com.task.tournaments.model.Participant;
import com.task.tournaments.model.Tournament;
import com.task.tournaments.service.MatchService;
import com.task.tournaments.service.ParticipantService;
import com.task.tournaments.service.TournamentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
public class ParticipantIntegrationTest {
    private final ParticipantService participantService;
    private final TournamentService tournamentService;
    private final MatchService matchService;

    @Autowired
    public ParticipantIntegrationTest(ParticipantService participantService, TournamentService tournamentService, MatchService matchService) {
        this.participantService = participantService;
        this.tournamentService = tournamentService;
        this.matchService = matchService;
    }

    @Test
    @Rollback
    void createParticipantTest() {
        Participant expected = new Participant();
        expected.setNickname("Participant_1");
        Participant actual = participantService.createOrUpdate(expected);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    public void updateParticipantTest() {
        Participant expected = new Participant();
        expected.setNickname("Participant_1");
        participantService.createOrUpdate(expected);
        expected.setNickname("Participant_2");
        Participant actual = participantService.createOrUpdate(expected);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void getAllParticipantsTest() {
        Participant participant1 = new Participant();
        participant1.setNickname("Participant_1");
        Participant participant2 = new Participant();
        participant2.setNickname("Participant_2");
        List<Participant> expected = Arrays.asList(participant1, participant2);
        participantService.createOrUpdate(participant1);
        participantService.createOrUpdate(participant2);
        List<Participant> actual = participantService.getAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    void getParticipantByIdTest() {
        Participant expected = new Participant();
        expected.setNickname("Participant_1");
        participantService.createOrUpdate(expected);

        Assertions.assertEquals(expected, participantService.getById(expected.getId()));
    }

    @Test
    @Rollback
    void deleteParticipantByIdTest() {
        Participant expected = new Participant();
        expected.setNickname("Participant_1");
        participantService.createOrUpdate(expected);

        participantService.deleteById(expected.getId());
        Assertions.assertThrows(EntityNotFoundException.class, () -> participantService.getById(expected.getId()));
    }

    @Test
    @Rollback
    void addParticipantToTournamentTest() {
        Participant participant = new Participant();
        participant.setNickname("Participant_1");
        participantService.createOrUpdate(participant);

        List<Participant> participants = new ArrayList<>();
        participants.add(participant);

        List<Participant> empty = new ArrayList<>();

        Tournament tournament = new Tournament();
        tournament.setTitle("Tournament_1");
        tournament.setParticipantsNumber(8);
        tournament.setParticipants(empty);
        tournamentService.createOrUpdate(tournament);

        Tournament expected = new Tournament();
        expected.setId(tournament.getId());
        expected.setTitle("Tournament_1");
        expected.setParticipantsNumber(8);
        expected.setParticipants(participants);

        Assertions.assertTrue(participantService.addParticipantToTournament(participant, tournament));
        Assertions.assertEquals(expected, tournament);
    }

    @Test
    @Rollback
    void removeParticipantFromTournamentTest() {
        Participant participant = new Participant();
        participant.setNickname("Participant_1");
        participantService.createOrUpdate(participant);

        List<Participant> participants = new ArrayList<>();
        participants.add(participant);
        List<Participant> empty = new ArrayList<>();

        Tournament tournament = new Tournament();
        tournament.setTitle("Tournament_1");
        tournament.setParticipantsNumber(8);
        tournament.setParticipants(participants);
        tournamentService.createOrUpdate(tournament);

        Tournament expected = new Tournament();
        expected.setId(tournament.getId());
        expected.setTitle("Tournament_1");
        expected.setParticipantsNumber(8);
        expected.setParticipants(empty);

        Assertions.assertTrue(participantService.removeParticipantFromTournament(participant, tournament));
        Assertions.assertEquals(expected, tournament);
    }

    @Test
    @Rollback
    void getLosersTest() {
        Participant winner = new Participant("Winner");
        Participant loser = new Participant("Loser");
        participantService.createOrUpdate(winner);
        participantService.createOrUpdate(loser);

        matchService.createOrUpdate(new Match(null, null, winner, loser, 5, 3));
        List<Match> matches = matchService.getAll();

        List<Participant> expected = new ArrayList<>();
        expected.add(loser);

        Assertions.assertEquals(expected, participantService.getLosers(matches));
    }

}
