package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.ArrayList;

public interface EventRepository extends JpaRepository<Event,Long> {
    ArrayList<Event> getAllByDate(LocalDate localDate);
    ArrayList<Event> getAllByStatusAndDate(String status, LocalDate date);
}
