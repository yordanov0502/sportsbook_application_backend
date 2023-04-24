package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class EventCacheService {
    private final EventRepository eventRepository;

    @Cacheable(value="events", key="#date")
    public ArrayList<Event> getAllEventsByDate(LocalDate date) {
        return eventRepository.getAllByDate(date);
    }

    @CachePut(value = "events", key="#date")
    public ArrayList<Event> updateEvents(LocalDate date){
        return eventRepository.getAllByDate(date);
    }

    @CacheEvict(value="events", allEntries = true,beforeInvocation = true)
    public void evictEvents(){}

    @Cacheable(value="event", key="#id")
    public Event getEventById(Long id){return eventRepository.getById(id);}

    @CachePut(value = "event", key="#event.id")
    public Event updateEvent(Event event){
        return eventRepository.save(event);
    }

    @CacheEvict(value="event", allEntries = true,beforeInvocation = true)
    public void evictEvent(){}

}
