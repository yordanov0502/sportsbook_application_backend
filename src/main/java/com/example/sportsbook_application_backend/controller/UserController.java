package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.dto.UserDTO;
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
    public String registration(@RequestBody UserDTO userDTO){
        if(userService.createUser(userDTO))
            return "User created successful!";
        else
            return "User with such credentials exists!";
    }

    @PostMapping("/login")
    public String getUser(@RequestBody LoginDTO loginDTO){
        if(userService.login(loginDTO))
            return "Logging successful!";
        else
            return "Wrong credentials!";
    }
}
