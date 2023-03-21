package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.dto.user.UserChangePasswordDTO;
import com.example.sportsbook_application_backend.model.dto.user.UserDTO;
import com.example.sportsbook_application_backend.model.mapper.*;
import com.example.sportsbook_application_backend.model.dto.user.UserLoginDTO;
import com.example.sportsbook_application_backend.model.dto.user.UserRegistrationDTO;
import com.example.sportsbook_application_backend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/registration")
    public UserDTO userRegistration(@RequestBody UserRegistrationDTO userRegistrationDTO){

        userService.validateRegistrationFields(userRegistrationDTO);
        userService.createUser(userRegistrationDTO);
        return userMapper.mapToUserDTO(userService.getUserByUsername(userRegistrationDTO.getUsername()));
    }


    @PostMapping("/login")
    public UserDTO userLogin(@RequestBody UserLoginDTO userLoginDTO){

        userService.validateLoginFields(userLoginDTO);
        userService.checkUserCredentials(userLoginDTO);
        return userMapper.mapToUserDTO(userService.getUserByUsername(userLoginDTO.getUsername()));
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