package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.service.BetService;
import com.example.sportsbook_application_backend.service.EventService;
import com.example.sportsbook_application_backend.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/external-api")
public class ExternalAPIController {

    @Autowired
    private LeagueService leagueService;
    @Autowired
    private EventService eventService;
    @Autowired
    private BetService betService;


    @GetMapping("/getFixtures")
    public ResponseEntity<String> getFixtures(@RequestParam String date){
        int numberOfFixtures = eventService.callAPIForFixtures(date);
        if(numberOfFixtures!=0)
            return new ResponseEntity<>(numberOfFixtures+" fixtures have been added to the Database.", HttpStatus.OK);
        else
            return new ResponseEntity<>("NONE fixtures have been added to the Database!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("getLeagues")
    public ResponseEntity<String> getLeagues(){
        leagueService.callAPIForLeagues();
        return new ResponseEntity<>("Leagues have been added to the Database successful.", HttpStatus.OK);
    }

    @GetMapping("getOdds")
    public ResponseEntity<String> getOdds(@RequestParam String date){
        int numberOfFixtures = betService.callAPIForOddsByDate(date);
        return new ResponseEntity<>("Odds have been added to "+numberOfFixtures+" fixtures.", HttpStatus.OK);
    }

}