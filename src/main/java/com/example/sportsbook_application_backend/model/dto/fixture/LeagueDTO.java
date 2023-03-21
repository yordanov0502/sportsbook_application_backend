package com.example.sportsbook_application_backend.model.dto.fixture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeagueDTO {
    private Long id;
    private String name;
    private String country;
}
