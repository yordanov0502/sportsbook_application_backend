package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.Role;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class UserCacheServiceTest {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserCacheService userCacheService;

    @BeforeEach
    void clear() {
        userCacheService.evictUser();
        userCacheService.evictUsernameAll();
        userCacheService.evictAllExistEmail();
        userCacheService.evictUsers();
    }

    @Test
    void getUserById() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);

        Mockito.when(userRepository.findUserByUserId(1L)).thenReturn(user);
        assertEquals(user,userCacheService.getUserById(1L));
        assertEquals(user,userCacheService.getUserById(1L));
        Mockito.verify(userRepository,Mockito.times(1)).findUserByUserId(1L);

        userCacheService.evictUser();
        assertEquals(user,userCacheService.getUserById(1L));
        Mockito.verify(userRepository,Mockito.times(2)).findUserByUserId(1L);
    }

    @Test
    void updateUser() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);

        Mockito.when(userRepository.save(user)).thenReturn(user);
        assertEquals(user, userCacheService.updateUser(user));
        Mockito.verify(userRepository,Mockito.times(1)).save(user);

        user.setLastName("Georgiev");
        assertEquals("Georgiev", userCacheService.updateUser(user).getLastName());
        Mockito.verify(userRepository,Mockito.times(2)).save(user);
    }

    @Test
    void getUserByUsername() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);

        Mockito.when(userRepository.findUserByUsername("gosho123")).thenReturn(user);
        assertEquals(user,userCacheService.getUserByUsername("gosho123"));
        assertEquals(user,userCacheService.getUserByUsername("gosho123"));
        Mockito.verify(userRepository,Mockito.times(1)).findUserByUsername("gosho123");

        userCacheService.evictUsername("gosho123");
        assertEquals(user,userCacheService.getUserByUsername("gosho123"));
        Mockito.verify(userRepository,Mockito.times(2)).findUserByUsername("gosho123");

        userCacheService.evictUsernameAll();
        assertEquals(user,userCacheService.getUserByUsername("gosho123"));
        Mockito.verify(userRepository,Mockito.times(3)).findUserByUsername("gosho123");
    }

    @Test
    void existEmail() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);

        Mockito.when(userRepository.existsUserByEmail("gosho123@abv.bg")).thenReturn(true);
        assertTrue(userCacheService.existEmail("gosho123@abv.bg"));
        assertTrue(userCacheService.existEmail("gosho123@abv.bg"));
        Mockito.verify(userRepository,Mockito.times(1)).existsUserByEmail("gosho123@abv.bg");

        userCacheService.evictExistEmail("gosho123@abv.bg");
        assertTrue(userCacheService.existEmail("gosho123@abv.bg"));
        Mockito.verify(userRepository,Mockito.times(2)).existsUserByEmail("gosho123@abv.bg");

        userCacheService.evictAllExistEmail();
        assertTrue(userCacheService.existEmail("gosho123@abv.bg"));
        Mockito.verify(userRepository,Mockito.times(3)).existsUserByEmail("gosho123@abv.bg");
    }

    @Test
    void getUsers() {
        User user1 = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);
        User user2 = new User(2L,"Ivan","Petrov","ivan776@abv.bg","Az$um_Iv@n776","ivan776",200F, UserStatus.ACTIVE, Role.USER);
        ArrayList<User> activeUsers = new ArrayList<>(Arrays.asList(user1,user2));

        Mockito.when(userRepository.getAllByStatus(UserStatus.ACTIVE)).thenReturn(activeUsers);

        assertEquals(2,userCacheService.getUsers(UserStatus.ACTIVE).size());
        assertEquals(user1,userCacheService.getUsers(UserStatus.ACTIVE).get(0));
        assertEquals(user2,userCacheService.getUsers(UserStatus.ACTIVE).get(1));
        Mockito.verify(userRepository,Mockito.times(1)).getAllByStatus(UserStatus.ACTIVE);

        userCacheService.evictUsers();
        assertEquals(2,userCacheService.getUsers(UserStatus.ACTIVE).size());
        Mockito.verify(userRepository,Mockito.times(2)).getAllByStatus(UserStatus.ACTIVE);
    }
}