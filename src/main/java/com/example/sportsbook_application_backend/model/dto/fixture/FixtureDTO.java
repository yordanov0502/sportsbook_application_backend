package com.example.sportsbook_application_backend.model.dto.fixture;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FixtureDTO {
    private Long id;
    @JsonProperty("date")
    private String datetime;
    private FixtureStatusDTO status;
}
