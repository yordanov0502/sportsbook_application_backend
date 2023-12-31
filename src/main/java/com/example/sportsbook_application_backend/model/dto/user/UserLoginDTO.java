package com.example.sportsbook_application_backend.model.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    private String username;
    private String password;
}
