package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.Bet;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import com.example.sportsbook_application_backend.repository.BetRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class BetCacheServiceTest {

    @MockBean
    private BetRepository betRepository;
    @Autowired
    private BetCacheService betCacheService;

    @BeforeEach
    public void clear() {
     betCacheService.evictBet();
     betCacheService.evictBetExistByEvent();
     betCacheService.evictBetsByEventAndOutcome();
    }

    @Test
    void getBetById() {
        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Match Finished", ResultType.TWO);
        Bet bet = new Bet(1L,event, Outcome.PENDING, ResultType.ONE,3.45F);

        Mockito.when(betRepository.getBetById(1L)).thenReturn(bet);
        assertEquals(bet,betCacheService.getBetById(1L));//call 1 [to database]
        assertEquals(bet.getType(),betCacheService.getBetById(1L).getType());//call 2 [to cache]
        assertEquals(bet.getOutcome(),betCacheService.getBetById(1L).getOutcome());//call 3 [to cache]
        Mockito.verify(betRepository,Mockito.times(1)).getBetById(1L); //1 call to database expected, because the other 2 calls are directly to the cache

        betCacheService.evictBet();//clear the bet cache
        assertEquals(bet,betCacheService.getBetById(1L));
        Mockito.verify(betRepository,Mockito.times(2)).getBetById(1L); //2nd call to database is executed, because we have previously cleared the bet cache
    }

    @Test
    void updateBet() {
        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Match Finished", ResultType.TWO);
        Bet bet = new Bet(1L,event, Outcome.PENDING, ResultType.ONE,3.45F);

        bet.setOutcome(Outcome.LOST);
        Mockito.when(betRepository.save(bet)).thenReturn(bet);
        assertEquals(Outcome.LOST, betCacheService.updateBet(bet).getOutcome());
        Mockito.verify(betRepository,Mockito.times(1)).save(bet);

        assertEquals(Outcome.LOST, betCacheService.updateBet(bet).getOutcome());
        Mockito.verify(betRepository,Mockito.times(2)).save(bet);
    }

    @Test
    void betExistByEvent(){
        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Match Finished", ResultType.TWO);
        Bet bet = new Bet(1L,event, Outcome.PENDING, ResultType.ONE,3.45F);

        Mockito.when(betRepository.existsBetByEvent(event)).thenReturn(true);
        assertTrue(betCacheService.betExistByEvent(event));
        assertTrue(betCacheService.betExistByEvent(event));
        Mockito.verify(betRepository,Mockito.times(1)).existsBetByEvent(event);

        betCacheService.evictBetExistByEvent();
        assertTrue(betCacheService.betExistByEvent(event));
        Mockito.verify(betRepository,Mockito.times(2)).existsBetByEvent(event);
    }

    @Test
    void betsByEventAndOutcome(){
        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Match Finished", ResultType.TWO);
        Bet bet1 = new Bet(1L,event, Outcome.PENDING, ResultType.ONE,3.45F);
        Bet bet2 = new Bet(2L,event, Outcome.PENDING, ResultType.ZERO,2.45F);
        Bet bet3 = new Bet(3L,event, Outcome.PENDING, ResultType.TWO,1.45F);
        ArrayList<Bet> pendingBets = new ArrayList<>(Arrays.asList(bet1,bet2,bet3));

        Mockito.when(betRepository.getBetByOutcomeAndEvent(Outcome.PENDING,event)).thenReturn(pendingBets);
        assertEquals(3,betCacheService.betsByEventAndOutcome(Outcome.PENDING,event).size());
        assertEquals(3,betCacheService.betsByEventAndOutcome(Outcome.PENDING,event).size());
        Mockito.verify(betRepository,Mockito.times(1)).getBetByOutcomeAndEvent(Outcome.PENDING,event);

        betCacheService.evictBetsByEventAndOutcome();
        assertEquals(3,betCacheService.betsByEventAndOutcome(Outcome.PENDING,event).size());
        Mockito.verify(betRepository,Mockito.times(2)).getBetByOutcomeAndEvent(Outcome.PENDING,event);

        betCacheService.evictBetsByEventAndOutcomeByEvent(event.getId());
        assertEquals(3,betCacheService.betsByEventAndOutcome(Outcome.PENDING,event).size());
        Mockito.verify(betRepository,Mockito.times(3)).getBetByOutcomeAndEvent(Outcome.PENDING,event);
    }

}