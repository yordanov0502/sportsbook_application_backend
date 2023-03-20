package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long> {
}
