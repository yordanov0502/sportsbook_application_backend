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
public class Value {
    @JsonProperty("value")
    private String value;
    @JsonProperty("odd")
    private String odd;
}