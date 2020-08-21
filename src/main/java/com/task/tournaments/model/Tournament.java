package com.task.tournaments.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tournaments")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Integer participantsNumber;

    private Integer matchesNumber;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Participant> participants;

    @OneToMany
    private List<Match> matches;

    public Tournament(String title, Integer participantsNumber, Integer matchesNumber) {
        this.title = title;
        this.participantsNumber = participantsNumber;
        this.matchesNumber = matchesNumber;
    }
}
