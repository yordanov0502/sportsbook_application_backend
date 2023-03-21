package com.example.sportsbook_application_backend.model.json.odd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bookmaker {
    @JsonProperty("id")
    private Long bookmaker_id;
    @JsonProperty("name")
    private String bookmaker_name;
    @JsonProperty("bets")
    private ArrayList<Bet> bets;
}