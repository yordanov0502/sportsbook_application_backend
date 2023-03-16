package com.example.sportsbook_application_backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}
