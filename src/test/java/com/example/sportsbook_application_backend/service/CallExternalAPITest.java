package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.exception.UpdateException;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.repository.BetRepository;
import com.example.sportsbook_application_backend.repository.EventRepository;
import com.example.sportsbook_application_backend.repository.LeagueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CallExternalAPITest {

    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private BetRepository betRepository;
    private LeagueService leagueService;
    private EventService eventService;
    private BetService betService;
    private LocalDate date;



    public RestTemplate restTemplate(RestTemplateBuilder builder){
        UriTemplateHandler uriTemplateHandler = new RootUriTemplateHandler("https://api-football-v1.p.rapidapi.com/v3/");
        return builder
                .uriTemplateHandler(uriTemplateHandler)
                .defaultHeader("X-RapidAPI-Key","7c6673cc3fmsh774315cfbb398dcp16717bjsn652b51eaa340")
                .defaultHeader("X-RapidAPI-Host","api-football-v1.p.rapidapi.com")
                .build();
    }

    @BeforeEach
    void setUp(){
        RestTemplate restTemplate = restTemplate(new RestTemplateBuilder());
        leagueService = new LeagueService(leagueRepository, restTemplate);
        eventService = new EventService(eventRepository,leagueService,restTemplate);
        betService = new BetService(betRepository,eventService,restTemplate);
        date = LocalDate.now();
    }

    void callAPIForLeagues() {
        int numberOfLeaguesAdded = leagueService.callAPIForLeagues();
        assertEquals(leagueService.getLeagues().size(),numberOfLeaguesAdded);
        assertThrowsExactly(UpdateException.class, () -> leagueService.callAPIForLeagues());
        assertEquals(leagueService.getAllowedLeagues().size(),24);

        League found=leagueService.getLeagueById(1L);
        assertThat(found.getLeague())
                .isEqualTo("World Cup");

        Long[] leagues = {2L, 3L, 5L, 10L, 39L, 40L, 45L, 61L, 78L};
        List<Long> leaguesList = List.of(leagues);
        for (League league:leagueService.getLeagues()){
            league.setAllowed(leaguesList.contains(league.getId()));
            leagueRepository.save(league);
        }
    }

    void callAPIForFixtures() {
        while((date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY))
        {
            date = date.plusDays(1);
        }

        int numberOfEventsAdded = eventService.callAPIForFixtures(date.toString());
        assertEquals(eventRepository.findAll().size(),numberOfEventsAdded);

        numberOfEventsAdded = eventService.callAPIForFixtures(date.minusDays(7).toString());
        assertEquals(eventRepository.getAllByDate(date.minusDays(7)).size(),numberOfEventsAdded);
    }

    void callAPIForOddsByDate(){
        int numberOfOddsAdded = betService.callAPIForOddsByDate(date.toString());
        assertEquals(betRepository.getAllBetsByDate(date).size(),numberOfOddsAdded);
    }

    @Test
    void callAPI(){
        callAPIForLeagues();
        callAPIForFixtures();
        callAPIForOddsByDate();
    }

}