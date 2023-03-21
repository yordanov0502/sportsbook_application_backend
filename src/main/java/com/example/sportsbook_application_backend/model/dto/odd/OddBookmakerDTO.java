package com.example.sportsbook_application_backend.model.dto.odd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OddBookmakerDTO {
    private Long id;
    private String name;
    private ArrayList<OddBetDTO> bets;
}