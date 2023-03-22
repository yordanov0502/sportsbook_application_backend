package com.example.sportsbook_application_backend.model.dto.league;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaguesDTO {
    private LeagueDTO league;
    private CountryDTO country;
}
