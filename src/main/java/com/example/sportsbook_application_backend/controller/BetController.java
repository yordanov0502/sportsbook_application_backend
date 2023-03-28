package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.entity.Slip;
import com.example.sportsbook_application_backend.service.BetService;
import com.example.sportsbook_application_backend.service.SlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class BetController {

    @Autowired
    BetService betService;
    @Autowired
    SlipService slipService;

    @PostMapping("/placeBet")
    public ResponseEntity<String> placeBet(@RequestParam Long userId,Long betId,Float stake){
         slipService.validateSlipParams(userId, betId, stake);
         slipService.placeBet(userId, betId, stake);
         return new ResponseEntity<>("A slip was created successfully.",HttpStatus.OK);
    }

    @GetMapping("/getBetHistory")
    public ResponseEntity<ArrayList<Slip>> getBetHistory(@RequestParam Long userId){
        ArrayList<Slip> slips = slipService.getBetHistoryByUserId(userId);
        return new ResponseEntity<>(slips,HttpStatus.OK);
    }
}