package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, Long> {
}