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

    @GetMapping()
    public UserDTO getProfile(@AuthenticationPrincipal User user){
        return userMapper.mapToUserDTO(user);
    }

    @PostMapping("/edit/information")
    public UserDTO editInformation(@RequestBody UserDTO userDTO) {
        userService.validateEditFields(userDTO);
        userService.editUser(userDTO);

        return userMapper.mapToUserDTO(userService.getUserById(userDTO.getId()));
    }

    @PostMapping("/edit/password")
    public UserDTO changePassword(@RequestBody UserChangePasswordDTO userChangePasswordDTO)
    {
        userService.validatePasswordChange(userChangePasswordDTO);
        userService.changePassword(userChangePasswordDTO);
        return userMapper.mapToUserDTO(userService.getUserById(userChangePasswordDTO.getId()));
    }
}