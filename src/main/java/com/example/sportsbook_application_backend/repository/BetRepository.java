package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {
}