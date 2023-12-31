package com.example.sportsbook_application_backend.model.dto.fixture;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class EventListDTO {
    @JsonProperty("response")
    private ArrayList<EventDTO> events;
}
