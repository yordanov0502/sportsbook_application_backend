package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.response.Response;
import com.example.sportsbook_application_backend.model.response.StatusResponse;
import com.example.sportsbook_application_backend.model.response.UserResponse;
import com.example.sportsbook_application_backend.model.dto.LoginDTO;
import com.example.sportsbook_application_backend.model.dto.RegistrationDTO;
import com.example.sportsbook_application_backend.service.UserService;
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
    public StatusResponse registration(@RequestBody RegistrationDTO userDTO){
        if(userService.createUser(userDTO))
            return new StatusResponse(201,"User created successful!","/registration");
        else
            return new StatusResponse(400,"User with such credentials exists!","/registration");
    }

    @PostMapping("/login")
    public Response getUser(@RequestBody LoginDTO loginDTO){
        if(userService.checkUserCredentials(loginDTO))
            return new UserResponse(userService.returnUser(loginDTO));
        else
            return new StatusResponse(400,"Wrong credentials!","/login");
    }
}
