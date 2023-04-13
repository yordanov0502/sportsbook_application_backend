package com.example.sportsbook_application_backend.model.dto.odd;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OddPage {
    private int current;
    private int total;
}