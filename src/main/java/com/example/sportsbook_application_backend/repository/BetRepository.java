package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.Bet;
import org.springframework.data.repository.CrudRepository;

public interface BetRepository extends CrudRepository<Bet, Long> {
}