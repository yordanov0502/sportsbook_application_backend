package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.dto.fixture.EventDTO;
import com.example.sportsbook_application_backend.model.dto.fixture.EventListDTO;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import com.example.sportsbook_application_backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

    public void callAPIForFixtures(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);

        for(League league:leagueService.getLeagues()) {
            EventListDTO eventsList = restTemplate.getForObject("/fixtures?date=" + parsedDate + "&league=" + league.getId() + "&season="+league.getSeason(), EventListDTO.class);
            for (EventDTO eventDTO : eventsList.getEvents()) {
                LocalDateTime dateTime = LocalDateTime.parse(eventDTO.getFixture().getDatetime().replace("+00:00", ""));

                Event event = new Event(eventDTO.getFixture().getId(), league, dateTime,parsedDate, eventDTO.getTeams().getHome().getName(), eventDTO.getTeams().getAway().getName(), eventDTO.getFixture().getStatus().getStatus(), null);

                if (eventDTO.getTeams().getHome().isWinner())
                    event.setResult(ResultType.ONE);
                else if (eventDTO.getTeams().getAway().isWinner())
                    event.setResult(ResultType.TWO);
                else if (event.getStatus().contains("Match Finished"))
                    event.setResult(ResultType.ZERO);

                eventRepository.save(event);
            }
        }
    }

    public ArrayList<Event> getAllFixturesByDate(LocalDate localDate) { return eventRepository.getAllByDate(localDate);}

}
