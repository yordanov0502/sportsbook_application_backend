package com.example.sportsbook_application_backend.model.entity;

import com.example.sportsbook_application_backend.model.enums.ResultType;
import com.example.sportsbook_application_backend.model.listener.EventListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="Events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(EventListener.class)
public class Event {
    @Id
    @Column(name = "id_event", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)//allows "foreign key on cascade delete"(deletes all events when the league they were associated with is deleted)
    @JoinColumn(name = "league_id",referencedColumnName = "id_league", nullable = false)
    private League league;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "home_team", nullable = false)
    private String homeTeam;

    @Column(name = "away_team", nullable = false)
    private String awayTeam;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "result")
    @Enumerated(EnumType.STRING)
    private ResultType result;

}
