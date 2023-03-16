package com.example.sportsbook_application_backend.model.response;


import com.example.sportsbook_application_backend.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse implements Response{
    private UserDTO userDTO;
}
