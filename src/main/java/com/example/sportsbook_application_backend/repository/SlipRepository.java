package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.Slip;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface SlipRepository extends JpaRepository<Slip, Long> {
    ArrayList<Slip> getAllByUserAndOutcome(User user, Outcome outcome);
}