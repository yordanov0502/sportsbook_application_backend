package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    League getLeagueById(Long id);
    ArrayList<League> getAllByAllowed(boolean allowed);
    int countAllByAllowed(boolean allowed);
    boolean existsLeagueById(Long id);
}