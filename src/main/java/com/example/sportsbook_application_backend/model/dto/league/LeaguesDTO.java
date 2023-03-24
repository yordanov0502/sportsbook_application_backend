package com.example.sportsbook_application_backend.model.dto.league;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaguesDTO {
    private LeagueDTO league;
    private CountryDTO country;
    private ArrayList<SeasonDTO> seasons;
}