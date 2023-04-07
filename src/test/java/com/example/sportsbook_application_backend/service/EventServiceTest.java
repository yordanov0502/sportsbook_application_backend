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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class EventServiceTest {
    @MockBean
    private EventRepository eventRepository;
    @Autowired
    private EventService eventService;

    @BeforeEach
    public void setUp() {
        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Not Started",null);
        Event event2 = new Event(2L,league, LocalDateTime.now(), LocalDate.now(),"Manchester United","Everton","Not Started",null);
        Event event3 = new Event(3L,league, LocalDateTime.now(), LocalDate.now(),"Tottenham","Brighton","Not Started",null);
        Event event4 = new Event(4L,league, LocalDateTime.now(), LocalDate.now(),"Newcastle","Liverpool","Finished", ResultType.ONE);
        Event event5 = new Event(5L,league, LocalDateTime.now(), LocalDate.now(),"Manchester City","Fulham","Not Started",null);
        Event event6 = new Event(6L,league, LocalDateTime.now(), LocalDate.now(),"Aston Villa","Brentford","Not Started",null);
        Event event7 = new Event(7L,league, LocalDateTime.now(), LocalDate.now(),"Southampton","West Ham","Not Started",null);
        Event event8 = new Event(8L,league, LocalDateTime.now().plusDays(1), LocalDate.now().plusDays(1),"Wolverhampton","Leicester","Not Started",null);
        Event event9 = new Event(9L,league, LocalDateTime.now().plusDays(1), LocalDate.now().plusDays(1),"Leeds United","Crystal Palace","Not Started",null);


        ArrayList<Event> events = new ArrayList<>();
        events.add(event);
        events.add(event2);
        events.add(event3);
        events.add(event4);
        events.add(event5);
        events.add(event6);
        events.add(event7);
        events.add(event8);
        events.add(event9);

        ArrayList<Event> eventsByDate=eventsByDate(events,LocalDate.now());

        Mockito.when(eventRepository.getAllByDate(LocalDate.now()))
                .thenReturn(eventsByDate);

        for (Event event1 : events) {
            Mockito.when(eventRepository.getById(event1.getId()))
                    .thenReturn(event1);
        }

        for (Event event1 : events) {
            Mockito.when(eventRepository.existsEventById(event1.getId()))
                    .thenReturn(true);
        }

        Mockito.when(eventRepository.getAllByStatusAndDate("Not Started", LocalDate.now()))
                .thenReturn(getAllByStatusAndDate(eventsByDate(events,LocalDate.now())));
        Mockito.when(eventRepository.getAllByStatusAndDate("Not Started", LocalDate.now().plusDays(1)))
                .thenReturn(getAllByStatusAndDate(eventsByDate(events,LocalDate.now().plusDays(1))));
        Mockito.when(eventRepository.getAllByStatusAndDate("Not Started", LocalDate.now().plusDays(2)))
                .thenReturn(getAllByStatusAndDate(eventsByDate(events,LocalDate.now().plusDays(2))));

    }

    public ArrayList<Event>eventsByDate(ArrayList<Event> events,LocalDate date){
        ArrayList<Event> eventsByDate=new ArrayList<>();
        for(Event event1:events){
            if(Objects.equals(event1.getDate(), date))
                eventsByDate.add(event1);
        }
        return eventsByDate;
    }

    public ArrayList<Event>getAllByStatusAndDate(ArrayList<Event> events){
        ArrayList<Event> events2=new ArrayList<>();
        for(Event event1:events){
            if(Objects.equals(event1.getStatus(), "Not Started"))
                events2.add(event1);
        }
        return events2;
    }

    @Test
    void getAllFixturesByDate() {
        ArrayList<Event> found=eventService.getAllFixturesByDate(LocalDate.now().toString());
        assertThat(found.size())
                .isEqualTo(7);
        found=eventService.getAllFixturesByDate(LocalDate.now().toString());
        assertThat(found.size())
                .isNotEqualTo(9);
    }

    @Test
    void getFixtureById() {
        Event found=eventService.getFixtureById(1L);
        assertThat(found.getLeague().getLeague())
                .isEqualTo("Premier league");
        assertThat(found.getStatus())
                .isNotEqualTo("Finished");
        assertThat(found.getHomeTeam())
                .isNotEqualTo("Arsenal");
        found=eventService.getFixtureById(2L);
        assertThat(found.getDate())
                .isEqualTo(LocalDate.now());
        assertThat(found.getDateTime())
                .isNotEqualTo(LocalDateTime.now());
        assertThat(found.getAwayTeam())
                .isEqualTo("Everton");
        found=eventService.getFixtureById(4L);
        assertThat(found.getResult())
                .isNotEqualTo(null);
        assertThat(found.getResult())
                .isEqualTo(ResultType.ONE);
    }

    @Test
    void fixtureExists() {
        boolean found=eventService.fixtureExists(1L);
        assertTrue(found);
        found=eventService.fixtureExists(10L);
        assertFalse(found);
    }

    @Test
    void simulateFixturesByDate() {
        int numberOfSimMatches = eventService.simulateFixturesByDate(LocalDate.now().toString());
        assertThat(numberOfSimMatches)
                .isNotEqualTo(9);
        assertThat(numberOfSimMatches)
                .isEqualTo(6);
        numberOfSimMatches = eventService.simulateFixturesByDate(LocalDate.now().plusDays(1).toString());
        assertThat(numberOfSimMatches)
                .isEqualTo(2);
        numberOfSimMatches = eventService.simulateFixturesByDate(LocalDate.now().plusDays(2).toString());
        assertThat(numberOfSimMatches)
                .isEqualTo(0);
    }
}