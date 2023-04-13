package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.dto.user.UserChangePasswordDTO;
import com.example.sportsbook_application_backend.model.dto.user.UserDTO;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.mapper.*;
import com.example.sportsbook_application_backend.model.dto.user.UserRegistrationDTO;
import com.example.sportsbook_application_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/registration")
    public UserDTO userRegistration(@RequestBody UserRegistrationDTO userRegistrationDTO){
        userService.validateRegistrationFields(userRegistrationDTO);
        userService.createUser(userRegistrationDTO);

        return userMapper.mapToUserDTO(userService.getUserByUsername(userRegistrationDTO.getUsername()));
    }

    @PostMapping("/login")
    public UserDTO userLogin(@AuthenticationPrincipal User user){
        return userMapper.mapToUserDTO(user);
    }

    @GetMapping("")
    public UserDTO getAccount(@AuthenticationPrincipal User user){
        return userMapper.mapToUserDTO(user);
    }

    @PutMapping("/edit/information")
    public UserDTO editInformation(@AuthenticationPrincipal User user,@RequestBody UserDTO userDTO) {
        userService.validateEditFields(user,userDTO);
        userService.editUser(user,userDTO);

        return userMapper.mapToUserDTO(userService.getUserById(userDTO.getId()));
    }

    @PutMapping("/edit/password")
    public UserDTO changePassword(@AuthenticationPrincipal User user,@RequestBody UserChangePasswordDTO userChangePasswordDTO)
    {
        userService.validatePasswordChange(user,userChangePasswordDTO);
        userService.changePassword(user,userChangePasswordDTO);
        return userMapper.mapToUserDTO(userService.getUserById(userChangePasswordDTO.getId()));
    }
}