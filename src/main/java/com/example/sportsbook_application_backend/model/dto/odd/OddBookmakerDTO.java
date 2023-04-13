package com.example.sportsbook_application_backend.model.dto.odd;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class OddBookmakerDTO {
    private Long id;
    private String name;
    private ArrayList<OddBetDTO> bets;
}