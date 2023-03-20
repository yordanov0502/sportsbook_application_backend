package com.example.sportsbook_application_backend.model.entity;

import com.example.sportsbook_application_backend.model.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name="Events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @Column(name = "id_event", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)//allows "foreign key on cascade delete"(deletes all events when the league they were associated with is deleted)
    @JoinColumn(name = "league_id",referencedColumnName = "id_league", nullable = false)
    private League league;

    @Column(name = "date_ttime", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "home_team", nullable = false)
    private String homeTeam;

    @Column(name = "away_team", nullable = false)
    private String awayTeam;

    @Column(name = "finished", nullable = false)
    private String status;

    @Column(name = "result")
    @Enumerated(EnumType.STRING)
    private Type result;

}
