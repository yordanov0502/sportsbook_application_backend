package com.example.sportsbook_application_backend.model.entity;

import com.example.sportsbook_application_backend.model.enums.Result;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="Events")
@Getter
@Setter
public class Event {
    @Id
    @Column(name = "id_event", nullable = false)
    private Long id;
    @Column(name = "date_ttime", nullable = false)
    private LocalDateTime dateTime;
    @Column(name = "home_team", nullable = false)
    private String homeTeam;
    @Column(name = "away_team", nullable = false)
    private String awayTeam;
    @Column(name = "finished", nullable = false)
    private String status;
    @Column(name = "result")
    private Result result;

}
