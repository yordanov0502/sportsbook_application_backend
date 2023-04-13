package com.example.sportsbook_application_backend.model.dto.slip;

import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class SlipDTO {
    private long slipId;
    private long betId;

    private String league;
    private String country;
    private String type;
    private int season;

    private LocalDateTime dateTime;
    private String homeTeam;
    private String awayTeam;
    private String matchStatus;
    private ResultType matchResult;

    private ResultType betType;
    private Outcome betOutcome;
    private Float betOdd;

    private Float slipStake;
    private Float expectedProfit;
    private Outcome slipOutcome;

}