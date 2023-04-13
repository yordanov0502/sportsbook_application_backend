package com.example.sportsbook_application_backend.model.dto.league;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class LeaguesResponseDTO {
    private ArrayList<LeaguesDTO> response;
}
