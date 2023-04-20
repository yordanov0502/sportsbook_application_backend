package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.service.BetService;
import com.example.sportsbook_application_backend.service.EventService;
import com.example.sportsbook_application_backend.service.LeagueService;
import com.example.sportsbook_application_backend.service.SlipService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
public class AdminController {

    private final LeagueService leagueService;
    private final EventService eventService;
    private final BetService betService;
    private final SlipService slipService;

    @PostMapping("/simulate")
    public ResponseEntity<String> simulateFixtures(@RequestParam @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Invalid field parameter. The field should contain parameter of format [yyyy-MM-dd].") String date){
        int numberOfFixtures = eventService.simulateFixturesByDate(date);
        return new ResponseEntity<>(numberOfFixtures+ " fixtures for date: "+date+" have been simulated successfully.", HttpStatus.OK);
    }

    @PostMapping("/allowLeague")
    public ResponseEntity<String> allowLeague(@RequestParam @Positive(message = "League id should be a positive number.") Long id){
        leagueService.checkForExistingLeagueId(id);
        String league = leagueService.allowLeague(id);
        return new ResponseEntity<>(league+" has been allowed.", HttpStatus.OK);
    }

    @PostMapping("/disallowLeague")
    public ResponseEntity<String> disallowLeague(@RequestParam @Positive(message = "League id should be a positive number.") Long id){
        leagueService.checkForExistingLeagueId(id);
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
    public ResponseEntity<ArrayList<Event>> getFixturesByDate(@RequestParam @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Invalid field parameter. The field should contain parameter of format [yyyy-MM-dd].") String date){
        ArrayList<Event> events = eventService.getAllFixturesByDate(date);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}
