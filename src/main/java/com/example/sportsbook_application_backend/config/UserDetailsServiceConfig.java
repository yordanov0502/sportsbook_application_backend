package com.example.sportsbook_application_backend.config;

import com.example.sportsbook_application_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceConfig implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final com.example.sportsbook_application_backend.model.entity.User profile = userRepository.findUserByUsername(username);
        if (profile == null) {
            throw new UsernameNotFoundException(username);
        }
        UserDetails user= User.withUsername(profile.getUsername()).password(profile.getHash()).roles(profile.getRole().toString()).build();
        return user;
    }

}
