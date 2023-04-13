package com.example.sportsbook_application_backend.config;

import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceConfig implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public User loadUserByUsername(String id) throws UsernameNotFoundException {
        final User profile = userRepository.findUserByUserId(Long.valueOf(id));
        if (profile == null) {
            throw new UsernameNotFoundException(id);
        }
        return profile;
    }

}
