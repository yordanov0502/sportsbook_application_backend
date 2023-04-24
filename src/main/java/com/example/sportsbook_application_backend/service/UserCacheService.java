package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UserCacheService {
    private final UserRepository userRepository;

    @Cacheable(value="user", key="#id")
    public User getUserById(Long id){return userRepository.findUserByUserId(id);}

    @CachePut(value = "user", key="#user.userId")
    public User updateUser(User user){
        return userRepository.save(user);
    }

    @CacheEvict(value="user", allEntries = true,beforeInvocation = true)
    public void evictUser(){}

    @Cacheable(value="username", key="#username")
    public User getUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    @CacheEvict(value="username", key="#username", beforeInvocation = true)
    public void evictUsername(String username){}

    @CacheEvict(value="username", allEntries = true,beforeInvocation = true)
    public void evictUsernameAll(){}

    @Cacheable(value="email", key="#email")
    public boolean existEmail(String email){
        return userRepository.existsUserByEmail(email);
    }

    @CacheEvict(value="email", key="#email", beforeInvocation = true)
    public void evictExistEmail(String email){}

    @CacheEvict(value="email", allEntries = true, beforeInvocation = true)
    public void evictAllExistEmail(){}

    @Cacheable(value="users", key="#status")
    public ArrayList<User> getUsers(UserStatus status) {
        return userRepository.getAllByStatus(status);
    }

    @CacheEvict(value="users", allEntries = true,beforeInvocation = true)
    public void evictUsers(){}

}
