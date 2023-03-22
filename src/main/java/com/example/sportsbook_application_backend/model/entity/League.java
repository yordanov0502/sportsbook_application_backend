package com.example.sportsbook_application_backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Leagues")
public class League {
    @Id
    @Column(name = "id_league", nullable = false)
    private Long id;

    @Column(name = "league", nullable = false)
    private String league;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "allowed", nullable = false)
    private boolean allowed;

}