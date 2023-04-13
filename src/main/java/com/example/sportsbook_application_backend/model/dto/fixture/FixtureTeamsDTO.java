package com.example.sportsbook_application_backend.model.dto.fixture;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FixtureTeamsDTO {
    private HomeTeamDTO home;
    private AwayTeamDTO away;
}
