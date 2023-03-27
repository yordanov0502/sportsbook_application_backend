package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.service.BetService;
import com.example.sportsbook_application_backend.service.EventService;
import com.example.sportsbook_application_backend.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private LeagueService leagueService;
    @Autowired
    private EventService eventService;
    @Autowired
    private BetService betService;

    @PostMapping("/simulate")
    public ResponseEntity<String> simulateFixtures(@RequestParam String date){
        int numberOfFixtures = eventService.simulateFixturesByDate(date);
        betService.resolveBets(date);
        return new ResponseEntity<>(numberOfFixtures+ " fixtures for date: "+date+" have been simulated successfully.", HttpStatus.OK);
    }

    @PostMapping("/allowLeague")
    public ResponseEntity<String> allowLeague(@RequestParam Long id){
        String league = leagueService.allowLeague(id);
        return new ResponseEntity<>(league+" has been allowed.", HttpStatus.OK);
    }

    @PostMapping("/disallowLeague")
    public ResponseEntity<String> disallowLeague(@RequestParam Long id){
        String league = leagueService.disallowLeague(id);
        return new ResponseEntity<>(league+" has been disallowed.", HttpStatus.OK);
    }

    @GetMapping("/getAllLeagues")
    public ResponseEntity<List<League>> getAllLeagues(){
        List<League> leagues = leagueService.getLeagues();
        return new ResponseEntity<>(leagues, HttpStatus.OK);
    }

    @GetMapping("/getAllowedLeagues")
    public ResponseEntity<ArrayList<League>> getAllowedLeagues(){
        ArrayList<League> leagues = leagueService.getAllowedLeagues();
        return new ResponseEntity<>(leagues, HttpStatus.OK);
    }

    @GetMapping("/getFixtures")
    public ResponseEntity<ArrayList<Event>> getFixturesByDate(@RequestParam String date){
        ArrayList<Event> events = eventService.getAllFixturesByDate(date);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
