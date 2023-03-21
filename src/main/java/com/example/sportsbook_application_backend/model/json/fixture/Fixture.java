package com.example.sportsbook_application_backend.model.json.fixture;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fixture {
    private Long id;
    @JsonProperty("date")
    private String datetime;
    private Status status;
}
