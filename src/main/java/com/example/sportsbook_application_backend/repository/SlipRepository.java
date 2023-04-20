package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.Slip;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
public interface SlipRepository extends JpaRepository<Slip, Long> {
    ArrayList<Slip> getAllByUserAndOutcome(User user, Outcome outcome);

    //get slips of user, which have certain outcome(PENDING) and which slips have certain bet outcome!=PENDING
    @Query("Select s from Slip s where s.user = :user and s.outcome = :outcome and s.bet.outcome not like \"PENDING\"")
    ArrayList<Slip> getAllByUserAndOutcomeAndBetOutcome(@Param("user")User user, @Param("outcome")Outcome outcome);

    int countAllByUser(User user);
    int countSlipsByUser(User user);

    @Query("Select s from Slip s where s.user = :user and s.outcome not like com.example.sportsbook_application_backend.model.enums.Outcome.PENDING")
    ArrayList<Slip> getExpiredBetsOfUser(@Param("user")User user);
}