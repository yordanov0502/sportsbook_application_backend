package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.dto.UserDTO;
import com.example.sportsbook_application_backend.model.mapper.*;
import com.example.sportsbook_application_backend.model.dto.UserLoginDTO;
import com.example.sportsbook_application_backend.model.dto.UserRegistrationDTO;
import com.example.sportsbook_application_backend.service.UserService;
import com.sun.net.httpserver.Authenticator;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public UserDTO userRegistration(@RequestBody UserRegistrationDTO userRegistrationDTO){

        userService.validateRegistrationFields(userRegistrationDTO);
        userService.createUser(userRegistrationDTO);
        return userService.mapToUserDTO(userRegistrationDTO.getUsername());
    }


    @PostMapping("/login")
    public UserDTO userLogin(@RequestBody UserLoginDTO userLoginDTO){

        userService.validateLoginFields(userLoginDTO);
        userService.checkUserCredentials(userLoginDTO);
        return userService.mapToUserDTO(userLoginDTO.getUsername());
    }
}