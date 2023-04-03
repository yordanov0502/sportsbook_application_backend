package com.example.sportsbook_application_backend.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
}
