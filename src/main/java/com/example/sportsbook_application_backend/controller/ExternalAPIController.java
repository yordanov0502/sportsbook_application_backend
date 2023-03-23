package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.service.BetService;
import com.example.sportsbook_application_backend.service.EventService;
import com.example.sportsbook_application_backend.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external-api")
public class ExternalAPIController {

    @Autowired
    private LeagueService leagueService;
    @Autowired
    private EventService eventService;
    @Autowired
    private BetService betService;


    @GetMapping("/getFixtures")
    public void getMatches(){eventService.callAPIForFixtures();}

    @GetMapping("/getLeagues")
    public void getLeagues(){leagueService.callAPIForLeagues();}

    @GetMapping("/getBets/{date}")
    public void getBets(@PathVariable String date){betService.callAPIForOddsByDate(date);}

}