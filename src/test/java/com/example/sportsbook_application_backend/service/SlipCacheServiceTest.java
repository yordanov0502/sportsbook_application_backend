package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.*;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import com.example.sportsbook_application_backend.model.enums.Role;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.repository.SlipRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class SlipCacheServiceTest {

    @MockBean
    private SlipRepository slipRepository;
    @Autowired
    private SlipCacheService slipCacheService;

    @BeforeEach
    void clear() {
        slipCacheService.evictHistory();
    }

    @Test
    void getHistory() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);
        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Match Finished", ResultType.TWO);
        Bet bet1 = new Bet(1L,event, Outcome.LOST, ResultType.ONE,2.45F);
        Bet bet2 = new Bet(2L,event, Outcome.LOST, ResultType.ZERO,3.45F);
        Slip slip1 = new Slip(1L,user,bet1,75F,183.75F,Outcome.LOST);
        Slip slip2 = new Slip(2L,user,bet2,75F,258.75F,Outcome.LOST);
        ArrayList<Slip> expiredSlips = new ArrayList<>(Arrays.asList(slip1,slip2));

        Mockito.when(slipRepository.getExpiredBetsOfUser(user)).thenReturn(expiredSlips);
        assertEquals(2,slipCacheService.getHistory(user).size());
        assertEquals(slip1,slipCacheService.getHistory(user).get(0));
        Mockito.verify(slipRepository,Mockito.times(1)).getExpiredBetsOfUser(user);

        slipCacheService.evictHistory();
        assertEquals(slip2,slipCacheService.getHistory(user).get(1));
        Mockito.verify(slipRepository,Mockito.times(2)).getExpiredBetsOfUser(user);
    }

    @Test
    void updateHistory() {
        User user = new User(1L,"Georgi","Ivanov","gosho123@abv.bg","Az$um_GOSHO123","gosho123",200F, UserStatus.ACTIVE, Role.USER);
        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Match Finished", ResultType.TWO);
        Bet bet1 = new Bet(1L,event, Outcome.LOST, ResultType.ONE,2.45F);
        Bet bet2 = new Bet(2L,event, Outcome.LOST, ResultType.ZERO,3.45F);
        Slip slip1 = new Slip(1L,user,bet1,75F,183.75F,Outcome.LOST);
        Slip slip2 = new Slip(2L,user,bet2,75F,258.75F,Outcome.PENDING);
        ArrayList<Slip> expiredSlips = new ArrayList<>(List.of(slip1));

        Mockito.when(slipRepository.getExpiredBetsOfUser(user)).thenReturn(expiredSlips);
        assertEquals(1,slipCacheService.getHistory(user).size());
        assertEquals(slip1,slipCacheService.getHistory(user).get(0));
        Mockito.verify(slipRepository,Mockito.times(1)).getExpiredBetsOfUser(user);

        slip2.setOutcome(Outcome.LOST);
        expiredSlips.add(slip2);
        Mockito.when(slipRepository.getExpiredBetsOfUser(user)).thenReturn(expiredSlips);
        assertEquals(2, slipCacheService.updateHistory(user).size());
        Mockito.verify(slipRepository,Mockito.times(2)).getExpiredBetsOfUser(user);
    }
}