package com.task.tournaments.repository;

import com.task.tournaments.model.Scores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoresRepository extends JpaRepository<Scores, Long> {
    Scores findByMatchIdAndParticipantId(Long matchId, Long participantId);
}
