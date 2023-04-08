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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
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
        Event event1 = new Event(1L,league, LocalDateTime.now().minusDays(3), LocalDate.now().minusDays(3),"Chelsea","Arsenal","Match Finished",ResultType.TWO);
        Event event2 = new Event(2L,league, LocalDateTime.now().minusDays(3), LocalDate.now().minusDays(3),"Manchester United","Everton","Match Finished",ResultType.ONE);
        Event event3 = new Event(3L,league, LocalDateTime.now().minusDays(3), LocalDate.now().minusDays(3),"Tottenham","Brighton","Match Finished",ResultType.ZERO);
        Event event4 = new Event(4L,league, LocalDateTime.now().minusDays(3), LocalDate.now().minusDays(3),"Newcastle","Liverpool","Match Finished", ResultType.ONE);

        Event event5 = new Event(5L,league, LocalDateTime.now().plusDays(3), LocalDate.now().plusDays(3),"Manchester City","Fulham","Not Started",null);

        Bet bet1 = new Bet(1L,event1, Outcome.PENDING, ResultType.TWO,3.45F);
        Bet bet2 = new Bet(2L,event2, Outcome.PENDING, ResultType.TWO,2.05F);
        Bet bet3 = new Bet(3L,event3, Outcome.PENDING, ResultType.TWO,5.13F);
        Bet bet4 = new Bet(4L,event4, Outcome.PENDING, ResultType.TWO,1.23F);

        Bet bet5 = new Bet(5L,event5, Outcome.PENDING, ResultType.TWO,2.85F);

        ArrayList<Bet> pendingBets = new ArrayList<>(Arrays.asList(bet1,bet2,bet3,bet4,bet5));
        when(betRepository.getBetByOutcome(Outcome.PENDING)).thenReturn(pendingBets);
        assertEquals(pendingBets.size(),betRepository.getBetByOutcome(Outcome.PENDING).size());
        verify(betRepository,times(1)).getBetByOutcome(Outcome.PENDING);

        assertEquals(4,betService.resolveBets(LocalDate.now().toString()));

        when(betRepository.getBetById(1L)).thenReturn(bet1);
        assertEquals(betRepository.getBetById(1L).getOutcome(),Outcome.WON);
        when(betRepository.getBetById(2L)).thenReturn(bet2);
        assertEquals(betRepository.getBetById(2L).getOutcome(),Outcome.LOST);
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