package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.dto.fixture.EventDTO;
import com.example.sportsbook_application_backend.model.dto.fixture.EventListDTO;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import com.example.sportsbook_application_backend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final LeagueService leagueService;
    private final RestTemplate restTemplate;


    public int callAPIForFixtures(String date){
        int numberOfFixtures=0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);

        for(League league:leagueService.getAllowedLeagues()) {
            EventListDTO eventsList = restTemplate.getForObject("/fixtures?date=" + parsedDate + "&league=" + league.getId() + "&season="+league.getSeason(), EventListDTO.class);
            for (EventDTO eventDTO : eventsList.getEvents()) {
                LocalDateTime dateTime = LocalDateTime.parse(eventDTO.getFixture().getDatetime().replace("+00:00", ""));

                Event event = new Event(eventDTO.getFixture().getId(), league, dateTime,parsedDate, eventDTO.getTeams().getHome().getName(), eventDTO.getTeams().getAway().getName(), eventDTO.getFixture().getStatus().getStatus(), null);

                if (eventDTO.getTeams().getHome().isWinner())
                    event.setResult(ResultType.ONE);
                else if (eventDTO.getTeams().getAway().isWinner())
                    event.setResult(ResultType.TWO);
                else if (event.getStatus().contains("Match Finished")||event.getStatus().contains("First Half")||event.getStatus().contains("Second Half")||event.getStatus().contains("Halftime")||event.getStatus().contains("Extra Time"))
                    event.setResult(ResultType.ZERO);
                else
                    event.setResult(null);

                eventRepository.save(event);
                numberOfFixtures++;
            }
        }
        return numberOfFixtures;
    }


    public ArrayList<Event> getAllFixturesByDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        return eventRepository.getAllByDate(parsedDate);
    }

    public Event getFixtureById(Long id){return eventRepository.getById(id);}

    public boolean fixtureExists(Long id){return eventRepository.existsEventById(id);}

    public int simulateFixturesByDate(String date){
        int numberOfFixtures=0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        ArrayList<Event> events=eventRepository.getAllByStatusAndDate("Not Started", localDate);
        for (Event event:events){
            event.setStatus("Match Finished");

            switch (sim()) {
                case 0 -> {
                    event.setResult(ResultType.ZERO);
                    eventRepository.save(event);
                }
                case 1 -> {
                    event.setResult(ResultType.ONE);
                    eventRepository.save(event);
                }
                case 2 -> {
                    event.setResult(ResultType.TWO);
                    eventRepository.save(event);
                }
            }
            numberOfFixtures++;
        }
        return numberOfFixtures;
    }

    public int sim(){
        return (int)(Math.random()*3);
    }

}
