package com.example.sportsbook_application_backend.model.dto.odd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OddPage {
    private int current;
    private int total;
}