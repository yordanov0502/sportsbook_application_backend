package com.example.sportsbook_application_backend.model.json.fixture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class League {
    private Long id;
    private String name;
    private String country;
}
