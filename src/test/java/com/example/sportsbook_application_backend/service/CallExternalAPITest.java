package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.exception.UpdateException;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.repository.LeagueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplateHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CallExternalAPITest {

    @Autowired
    private LeagueRepository leagueRepository;
    private LeagueService leagueService;

    public RestTemplate restTemplate(RestTemplateBuilder builder){
        UriTemplateHandler uriTemplateHandler = new RootUriTemplateHandler("https://api-football-v1.p.rapidapi.com/v3/");
        return builder
                .uriTemplateHandler(uriTemplateHandler)
                .defaultHeader("X-RapidAPI-Key","2abe9f6666msh12e81b74ce79b38p1f70a7jsnc9695ddd515a")
                .defaultHeader("X-RapidAPI-Host","api-football-v1.p.rapidapi.com")
                .build();
    }

    @BeforeEach
    void setUp(){
        RestTemplate restTemplate = restTemplate(new RestTemplateBuilder());
        leagueService= new LeagueService(leagueRepository, restTemplate);
    }

    @Test
    void callAPIForLeagues() {
        int numberOfLeaguesAdded = leagueService.callAPIForLeagues();
        assertEquals(leagueService.getLeagues().size(),numberOfLeaguesAdded);
        assertThrowsExactly(UpdateException.class, () -> leagueService.callAPIForLeagues());
        assertEquals(leagueService.getAllowedLeagues().size(),24);

        League found=leagueService.getLeagueById(1L);
        assertThat(found.getLeague())
                .isEqualTo("World Cup");
    }
}