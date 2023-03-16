package com.example.sportsbook_application_backend.model.response;


import com.example.sportsbook_application_backend.model.dto.UserDTO;

public class UserResponse implements Response{
    private UserDTO userDTO;

    public UserResponse(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
