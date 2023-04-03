package com.example.sportsbook_application_backend.repository;

import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
    User findUserByUsername(String username);
    User findUserByUserId(Long id);
    ArrayList<User> getAllByStatus(UserStatus status);

    Optional<User> findByUsername(String username);
}