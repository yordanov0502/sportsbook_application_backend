package com.example.sportsbook_application_backend.model.json.odd;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fixture {
    @JsonProperty("id")
    private Long fixture_id;
}