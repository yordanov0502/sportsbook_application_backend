package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import com.example.sportsbook_application_backend.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class EventCacheServiceTest {

    @MockBean
    private EventRepository eventRepository;
    @Autowired
    private EventCacheService eventCacheService;

    @BeforeEach
    public void clear(){
        eventCacheService.evictEvent();
        eventCacheService.evictEvents();
    }

    @Test
    void getAllEventsByDate() {
        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Not Started",null);
        ArrayList<Event> events = new ArrayList<>();
        events.add(event);
        Mockito.when(eventRepository.getAllByDate(LocalDate.now())).thenReturn(events);
        ArrayList<Event> found = eventCacheService.getAllEventsByDate(LocalDate.now());
        eventCacheService.getAllEventsByDate(LocalDate.now());

        assertEquals(found.size(),events.size());
        verify(eventRepository,times(1)).getAllByDate(LocalDate.now());

        Event event2 = new Event(2L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Not Started",null);
        events.add(event2);
        Mockito.when(eventRepository.getAllByDate(LocalDate.now())).thenReturn(events);
        eventCacheService.updateEvents(LocalDate.now());
        found = eventCacheService.getAllEventsByDate(LocalDate.now());
        assertEquals(found.size(),2);

        verify(eventRepository,times(2)).getAllByDate(LocalDate.now());

        eventCacheService.evictEvents();
        eventCacheService.getAllEventsByDate(LocalDate.now());
        verify(eventRepository,times(3)).getAllByDate(LocalDate.now());
    }

    @Test
    void getEventById() {
        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Not Started",null);
        Mockito.when(eventRepository.getById(1L)).thenReturn(event);
        Event found = eventCacheService.getEventById(1L);
        assertNull(found.getResult());

        eventCacheService.getEventById(1L);

        verify(eventRepository,times(1)).getById(1L);

        event.setResult(ResultType.ONE);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        eventCacheService.updateEvent(event);
        found = eventCacheService.getEventById(1L);
        assertEquals(found.getResult(),ResultType.ONE);

        verify(eventRepository,times(1)).getById(1L);

        eventCacheService.evictEvent();
        eventCacheService.getEventById(1L);
        verify(eventRepository,times(2)).getById(1L);
    }
}