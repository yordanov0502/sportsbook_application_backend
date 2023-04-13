package com.example.sportsbook_application_backend.model.dto.fixture;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventDTO {
    private FixtureDTO fixture;
    private FixtureLeagueDTO league;
    private FixtureTeamsDTO teams;
}
