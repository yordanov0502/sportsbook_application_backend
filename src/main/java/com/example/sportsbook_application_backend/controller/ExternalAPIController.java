package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.service.BetService;
import com.example.sportsbook_application_backend.service.EventService;
import com.example.sportsbook_application_backend.service.LeagueService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/external-api")
public class ExternalAPIController {

    private final LeagueService leagueService;
    private final EventService eventService;
    private final BetService betService;

    @GetMapping("/getLeagues")
    public ResponseEntity<String> getLeagues(){
        int numberOfLeagues = leagueService.callAPIForLeagues();
        return new ResponseEntity<>(numberOfLeagues+" leagues have been added to the database successfully.", HttpStatus.OK);
    }

    @GetMapping("/getFixtures")
    public ResponseEntity<String> getFixtures(@RequestParam @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Invalid field parameter. The field should contain parameter of format [yyyy-MM-dd].") String date){

        int numberOfFixtures = eventService.callAPIForFixtures(date);

        if(numberOfFixtures!=0)
            return new ResponseEntity<>(numberOfFixtures+" fixtures have been added to the database.", HttpStatus.OK);
        else
            return new ResponseEntity<>("NONE fixtures have been added to the database!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getOdds")
    public ResponseEntity<String> getOdds(@RequestParam @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Invalid field parameter. The field should contain parameter of format [yyyy-MM-dd].") String date){
        int numberOfFixtures = betService.callAPIForOddsByDate(date);
        return new ResponseEntity<>("Odds have been added to "+numberOfFixtures+" fixtures.", HttpStatus.OK);
    }

}