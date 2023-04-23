package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.Bet;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.ArrayList;

public interface BetRepository extends JpaRepository<Bet, Long> {
    @Query("Select b from Bet b where b.event.date = :date")
    ArrayList<Bet> getAllBetsByDate(@Param("date")LocalDate date);
    ArrayList<Bet> getBetByOutcomeAndEvent(Outcome outcome,Event event);
    Bet getBetById(Long id);
    boolean existsBetByEvent(Event event);
}