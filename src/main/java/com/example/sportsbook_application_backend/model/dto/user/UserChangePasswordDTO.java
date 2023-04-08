package com.example.sportsbook_application_backend.model.dto.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserChangePasswordDTO {
    private Long id;
    private String oldPassword;
    private String newPassword;
}