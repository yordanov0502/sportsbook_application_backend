package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.dto.fixture.EventDTO;
import com.example.sportsbook_application_backend.model.dto.fixture.EventListDTO;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.model.enums.Type;
import com.example.sportsbook_application_backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class EventService {
    private final EventRepository eventRepository;
    @Autowired
    private LeagueService leagueService;
    @Autowired
    private RestTemplate restTemplate;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void callAPIForFixtures(){
        for(League league:leagueService.getLeagues()) {
            EventListDTO eventsList = restTemplate.getForObject("/fixtures?date=" + LocalDate.now() + "&league=" + league.getId() + "&season="+league.getSeason(), EventListDTO.class);
            for (EventDTO eventDTO : eventsList.getEvents()) {
                LocalDateTime dateTime = LocalDateTime.parse(eventDTO.getFixture().getDatetime().replace("+00:00", ""));
                Event event = new Event(eventDTO.getFixture().getId(), league, dateTime, eventDTO.getTeams().getHome().getName(), eventDTO.getTeams().getAway().getName(), eventDTO.getFixture().getStatus().getStatus(), null);

                if (eventDTO.getTeams().getHome().isWinner())
                    event.setResult(Type.ONE);
                else if (eventDTO.getTeams().getAway().isWinner())
                    event.setResult(Type.TWO);
                else if (event.getStatus().contains("Match Finished"))
                    event.setResult(Type.ZERO);

                eventRepository.save(event);
            }
        }
    }
}
