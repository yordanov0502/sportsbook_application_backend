package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.service.BetService;
import com.example.sportsbook_application_backend.service.EventService;
import com.example.sportsbook_application_backend.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void getFixtures(@RequestParam String date){eventService.callAPIForFixtures(date);}

    @GetMapping("/getLeagues")
    public void getLeagues(){leagueService.callAPIForLeagues();}

    @GetMapping("/getBets")
    public void getBets(@RequestParam String date){betService.callAPIForOddsByDate(date);}

}