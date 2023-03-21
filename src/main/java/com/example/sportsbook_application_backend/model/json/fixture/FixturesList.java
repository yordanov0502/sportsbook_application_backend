package com.example.sportsbook_application_backend.model.json.fixture;

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
public class FixturesList {
    @JsonProperty("response")
    private ArrayList<Fixtures> fixtures;
}
