package com.example.sportsbook_application_backend.model.dto.fixture;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FixtureStatusDTO {
    @JsonProperty("long")
    private String status;
}
