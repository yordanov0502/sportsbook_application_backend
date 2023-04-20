package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.Bet;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import com.example.sportsbook_application_backend.repository.BetRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class BetServiceTest {

    @Mock private BetRepository betRepository;
    @InjectMocks private BetService betService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void resolveBets() {
        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        Event event1 = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Match Finished",ResultType.TWO);

        Bet bet1 = new Bet(1L,event1, Outcome.PENDING, ResultType.ONE,3.45F);
        Bet bet2 = new Bet(2L,event1, Outcome.PENDING, ResultType.TWO,2.05F);
        Bet bet3 = new Bet(3L,event1, Outcome.PENDING, ResultType.ZERO,5.13F);

        ArrayList<Bet> pendingBets = new ArrayList<>(Arrays.asList(bet1,bet2,bet3));
        when(betRepository.getBetByOutcomeAndEvent(Outcome.PENDING,event1)).thenReturn(pendingBets);
        assertEquals(pendingBets.size(),betRepository.getBetByOutcomeAndEvent(Outcome.PENDING,event1).size());
        verify(betRepository,times(1)).getBetByOutcomeAndEvent(Outcome.PENDING,event1);

        assertEquals(3,betService.resolveBets(event1));

        when(betRepository.getBetById(1L)).thenReturn(bet1);
        assertEquals(betRepository.getBetById(1L).getOutcome(),Outcome.LOST);
        when(betRepository.getBetById(2L)).thenReturn(bet2);
        assertEquals(betRepository.getBetById(2L).getOutcome(),Outcome.WON);
    }

    @Test
    void isBetExists() {
        when(betRepository.existsById(1L)).thenReturn(true);
        assertTrue(betService.isBetExists(1L));
        verify(betRepository,times(1)).existsById(1L);

        when(betRepository.existsById(2L)).thenReturn(false);
        assertFalse(betService.isBetExists(2L));
        verify(betRepository,times(1)).existsById(2L);
    }

    @Test
    void getBetById() {
        League league = new League(2L, "Premier league", "England", "League", 2022, true);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Not Started",null);
        Bet bet = new Bet(1L,event, Outcome.PENDING, ResultType.TWO,3.45F);
        when(betRepository.getBetById(1L)).thenReturn(bet);
        assertEquals(bet,betService.getBetById(1L));
        verify(betRepository,times(1)).getBetById(1L);
    }
}