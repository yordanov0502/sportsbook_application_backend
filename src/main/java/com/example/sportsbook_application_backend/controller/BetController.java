package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.dto.slip.SlipDTO;
import com.example.sportsbook_application_backend.service.SlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/bet")
public class BetController {

    @Autowired
    private BetService betService;
    @Autowired
    private SlipService slipService;


    @PostMapping("/placeBet")
    public ResponseEntity<String> placeBet(@RequestParam Long userId,@RequestParam Long betId, @RequestParam Float stake){
         slipService.validateSlipParams(userId, betId, stake);
         slipService.placeBet(userId, betId, stake);
         return new ResponseEntity<>("A slip was created successfully.",HttpStatus.OK);
    }

    @GetMapping("/getBetHistory")
    public ResponseEntity<ArrayList<SlipDTO>> getBetHistory(@RequestParam Long userId){
        return new ResponseEntity<>(slipService.getBetHistoryByUserId(userId),HttpStatus.OK);
    }
}