package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
    boolean existsUserByUsernameAndHash(String username,String hashedPassword);
    void deleteUserByUsernameAndHash(String username,String hashedPassword);
    User findUserByUsername(String username);
    User findUserByUserId(Long id);
    ArrayList<User> getAllByStatus(UserStatus status);
}