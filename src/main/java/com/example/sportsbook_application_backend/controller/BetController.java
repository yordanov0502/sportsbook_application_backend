package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.dto.slip.SlipDTO;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.service.EventService;
import com.example.sportsbook_application_backend.service.SlipService;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/user/bet")
public class BetController {

    private final SlipService slipService;
    private final EventService eventService;


    @PostMapping("/placeBet")
    public ResponseEntity<String> placeBet(
            @AuthenticationPrincipal User user,
            @RequestParam @Positive(message = "Bet id should be a positive number.") Long betId,
            @RequestParam @Positive(message = "Stake should be more than 0$.") Float stake) {
         slipService.checkForExistingParams(user.getUserId(), betId);
         slipService.placeBet(user.getUserId(), betId, stake);
         return new ResponseEntity<>("A slip was created successfully.",HttpStatus.OK);
    }

    @GetMapping("/getBetHistory")
    public ResponseEntity<ArrayList<SlipDTO>> getBetHistory(@AuthenticationPrincipal User user){
        return new ResponseEntity<>(slipService.getBetHistoryByUserId(user.getUserId()),HttpStatus.OK);
    }

    @GetMapping("/getEvents")
    public ResponseEntity<ArrayList<Event>> getEvents(@RequestParam @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Invalid field parameter. The field should contain parameter of format [yyyy-MM-dd].") String date){
        return new ResponseEntity<>(eventService.getAllFixturesByDate(date),HttpStatus.OK);
    }
}