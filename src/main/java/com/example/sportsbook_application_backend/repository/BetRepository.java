package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.Bet;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface BetRepository extends JpaRepository<Bet, Long> {
    ArrayList<Bet> getBetByOutcome(Outcome outcome);
    Bet getBetById(Long id);
    boolean existsBetByEvent(Event event);
}