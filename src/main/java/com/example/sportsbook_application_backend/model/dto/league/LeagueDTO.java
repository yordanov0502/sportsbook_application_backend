package com.example.sportsbook_application_backend.model.dto.league;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LeagueDTO {
    private Long id;
    private String name;
    private String type;
}
