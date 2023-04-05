package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.dto.slip.SlipDTO;
import com.example.sportsbook_application_backend.service.SlipService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/user/bet")
public class BetController {

    private final SlipService slipService;


    @PostMapping("/placeBet")
    public ResponseEntity<String> placeBet(
            @RequestParam @Positive(message = "User id should be a positive number.") Long userId,
            @RequestParam @Positive(message = "Bet id should be a positive number.") Long betId,
            @RequestParam @Positive(message = "Stake should be more than 0$.") Float stake) {
         slipService.checkForExistingParams(userId, betId);
         slipService.placeBet(userId, betId, stake);
         return new ResponseEntity<>("A slip was created successfully.",HttpStatus.OK);
    }

    @GetMapping("/getBetHistory")
    public ResponseEntity<ArrayList<SlipDTO>> getBetHistory(
            @RequestParam @Positive(message = "User id should be a positive number.") Long userId){
        return new ResponseEntity<>(slipService.getBetHistoryByUserId(userId),HttpStatus.OK);
    }
}