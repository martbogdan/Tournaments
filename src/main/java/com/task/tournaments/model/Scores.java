package com.task.tournaments.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"match_id", "participant_id"})})
public class Scores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer scores;

    @ManyToOne
    private Match match;

    @ManyToOne
    private Participant participant;
}
