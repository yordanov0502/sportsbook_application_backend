package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event,Long> {
}
