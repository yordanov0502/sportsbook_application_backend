package com.example.sportsbook_application_backend.model.mapper;

import com.example.sportsbook_application_backend.model.dto.UserDTO;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO mapToUserDTO(User user)
    {
        return UserDTO.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .balance(user.getBalance())
                .build();
    }
}
