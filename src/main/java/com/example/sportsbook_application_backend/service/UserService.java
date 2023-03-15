package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.dto.LoginDTO;
import com.example.sportsbook_application_backend.model.dto.UserDTO;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean createUser(UserDTO userDTO){
        if(!userRepository.existsUserByUsername(userDTO.getUsername()) && !userRepository.existsUserByEmail(userDTO.getEmail())){
            String password=userDTO.getPassword();
            User user=new User();
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setUsername(userDTO.getUsername());
            user.setHash(passwordEncoder.encode(password));
            user.setBalance(200F);
            user.setStatus("unlocked");
            userRepository.save(user);
            return true;
        }
        else {
            return false;
        }
    }

    public boolean login(LoginDTO loginDTO){
        User user = userRepository.findUserByUsername(loginDTO.getUsername());
        return passwordEncoder.matches(loginDTO.getPassword(), user.getHash());
    }

}
