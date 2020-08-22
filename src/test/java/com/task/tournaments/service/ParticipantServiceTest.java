package com.task.tournaments.service;

import com.task.tournaments.model.Match;
import com.task.tournaments.model.Participant;
import com.task.tournaments.model.Tournament;
import com.task.tournaments.repository.ParticipantRepository;
import com.task.tournaments.repository.TournamentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ParticipantServiceTest {
    private final ParticipantService participantService;

    @Autowired
    public ParticipantServiceTest(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @MockBean
    private ParticipantRepository participantRepository;
    @MockBean
    private TournamentRepository tournamentRepository;

    @Test
    void createParticipantTest() {
        Participant participant = new Participant();
        participant.setNickname("Participant_1");

        Mockito.doReturn(participant).when(participantRepository).save(participant);
        Participant actual = participantService.createOrUpdate(participant);

        Assertions.assertEquals(participant, actual);
        Mockito.verify(participantRepository, Mockito.times(1)).save(participant);
    }

    @Test
    public void updateParticipantTest() {
        Long id = 1L;
        Participant expected = new Participant();
        expected.setId(id);
        expected.setNickname("Participant_1");

        Mockito.doReturn(expected).when(participantRepository).save(expected);
        expected.setNickname("Participant_2");
        Mockito.doReturn(expected).when(participantRepository).save(expected);
        Participant actual = participantService.createOrUpdate(expected);
        Assertions.assertEquals(expected, actual);
        Mockito.verify(participantRepository, Mockito.times(1)).save(expected);
    }

    @Test
    void getAllParticipantsTest() {
        Participant participant1 = new Participant();
        participant1.setNickname("Participant_1");
        Participant participant2 = new Participant();
        participant2.setNickname("Participant_2");
        List<Participant> expected = Arrays.asList(participant1, participant2);
        Mockito.doReturn(expected).when(participantRepository).findAll();
        List<Participant> actual = participantService.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getParticipantByIdTest() {
        Long id = 1L;
        Participant expected = new Participant();
        expected.setId(id);
        expected.setNickname("Participant_1");

        Mockito.when(participantRepository.findById(id)).thenReturn(Optional.of(expected));
        Assertions.assertEquals(expected, participantService.getById(id));
    }

    @Test
    void deleteParticipantByIdTest() {
        Long id = 1L;
        Participant expected = new Participant();
        expected.setId(id);
        expected.setNickname("Participant_1");

        Mockito.when(participantRepository.findById(id)).thenReturn(Optional.of(expected));
        Mockito.when(participantRepository.existsById(expected.getId())).thenReturn(false);
        participantService.deleteById(id);
        Assertions.assertFalse(participantRepository.existsById(id));
    }

    @Test
    void addParticipantToTournamentTest() {
        Long id = 1L;
        Participant participant = new Participant();
        participant.setId(id);
        participant.setNickname("Participant_1");

        List<Participant> participants = new ArrayList<>();
        participants.add(participant);

        List<Participant> empty = new ArrayList<>();

        Tournament tournament = new Tournament();
        tournament.setId(id);
        tournament.setTitle("Tournament_1");
        tournament.setParticipantsNumber(8);
        tournament.setParticipants(empty);

        Tournament expected = new Tournament();
        expected.setId(id);
        expected.setTitle("Tournament_1");
        expected.setParticipantsNumber(8);
        expected.setParticipants(participants);

        Mockito.when(participantRepository.getOne(id)).thenReturn(participant);
        Mockito.when(tournamentRepository.getOne(id)).thenReturn(tournament);
        Mockito.when(tournamentRepository.save(tournament)).thenReturn(expected);

        Assertions.assertTrue(participantService.addParticipantToTournament(participant, tournament));
    }

    @Test
    void removeParticipantFromTournamentTest() {
        Long id = 1L;
        Participant participant = new Participant();
        participant.setId(id);
        participant.setNickname("Participant_1");

        List<Participant> participants = new ArrayList<>();
        participants.add(participant);
        List<Participant> empty = new ArrayList<>();

        Tournament tournament = new Tournament();
        tournament.setId(id);
        tournament.setTitle("Tournament_1");
        tournament.setParticipantsNumber(8);
        tournament.setParticipants(participants);

        Tournament expected = new Tournament();
        expected.setId(id);
        expected.setTitle("Tournament_1");
        expected.setParticipantsNumber(8);
        expected.setParticipants(empty);

        Mockito.when(participantRepository.getOne(id)).thenReturn(participant);
        Mockito.when(tournamentRepository.getOne(id)).thenReturn(tournament);
        Mockito.when(tournamentRepository.save(tournament)).thenReturn(expected);

        Assertions.assertTrue(participantService.removeParticipantFromTournament(participant, tournament));
    }

    @Test
    void getLosersTest() {
        Participant winner = new Participant("Winner");
        Participant loser = new Participant("Loser");
        List<Match> matches = new ArrayList<>();
        matches.add(new Match(null, null, winner, loser, 5, 3));

        List<Participant> expected = new ArrayList<>();
        expected.add(loser);

        Assertions.assertEquals(expected, participantService.getLosers(matches));
    }
}
