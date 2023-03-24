package com.example.sportsbook_application_backend.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserChangePasswordDTO {
    private Long id;
    private String oldPassword;
    private String newPassword;
}