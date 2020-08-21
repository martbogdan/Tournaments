package com.task.tournaments.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    @OneToOne
    private Participant participant1;
    @OneToOne
    private Participant participant2;

    private Integer score1;
    private Integer score2;

    public Match(LocalDateTime startTime, LocalDateTime finishTime, Participant participant1, Participant participant2, Integer score1, Integer score2) {
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.score1 = score1;
        this.score2 = score2;
    }
}
