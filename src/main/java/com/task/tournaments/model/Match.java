package com.task.tournaments.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

//    @ManyToMany
//    private List<Participant> participants;



    public Match(LocalDateTime startTime, LocalDateTime finishTime) {
        this.startTime = startTime;
        this.finishTime = finishTime;
    }
}
