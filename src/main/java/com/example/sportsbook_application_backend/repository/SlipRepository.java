package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.Slip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlipRepository extends JpaRepository<Slip, Long> {
}