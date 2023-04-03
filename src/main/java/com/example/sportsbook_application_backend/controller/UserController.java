package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.config.JwtService;
import com.example.sportsbook_application_backend.model.dto.user.UserChangePasswordDTO;
import com.example.sportsbook_application_backend.model.dto.user.UserDTO;
import com.example.sportsbook_application_backend.model.mapper.*;
import com.example.sportsbook_application_backend.model.dto.user.UserLoginDTO;
import com.example.sportsbook_application_backend.model.dto.user.UserRegistrationDTO;
import com.example.sportsbook_application_backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public UserController(UserService userService, UserMapper userMapper, JwtService jwtService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    @PostMapping("/registration")
    public UserDTO userRegistration(@RequestBody UserRegistrationDTO userRegistrationDTO, HttpServletResponse httpServletResponse){

        userService.validateRegistrationFields(userRegistrationDTO);
        userService.createUser(userRegistrationDTO);

        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generateToken(userService.getUserByUsername(userRegistrationDTO.getUsername())));
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        return userMapper.mapToUserDTO(userService.getUserByUsername(userRegistrationDTO.getUsername()));
    }


    @PostMapping("/login")
    public UserDTO userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse httpServletResponse){

        userService.validateLoginFields(userLoginDTO);
        userService.validateUser(userLoginDTO.getUsername(),userLoginDTO.getPassword());

        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generateToken(userService.getUserByUsername(userLoginDTO.getUsername())));
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        return userMapper.mapToUserDTO(userService.getUserByUsername(userLoginDTO.getUsername()));
    }


    @PostMapping("/edit/information")
    public UserDTO editInformation(@RequestBody UserDTO userDTO, HttpServletResponse httpServletResponse) {

        userService.validateEditFields(userDTO);
        userService.editUser(userDTO);

        httpServletResponse.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generateToken(userService.getUserByUsername(userDTO.getUsername())));
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

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