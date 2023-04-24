package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.Bet;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.repository.BetRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class BetCacheService {
    private final BetRepository betRepository;

    @Cacheable(value="bet", key="#id")
    public Bet getBetById(Long id){return betRepository.getBetById(id);}

    @CachePut(value = "bet", key="#bet.id")
    public Bet updateBet(Bet bet){
        return betRepository.save(bet);
    }

    @CacheEvict(value="bet", allEntries = true,beforeInvocation = true)
    public void evictBet(){}

    @Cacheable(value="betExistByEvent", key="#event.id")
    public boolean betExistByEvent(Event event){return betRepository.existsBetByEvent(event);}

    @CacheEvict(value="betExistByEvent", allEntries = true,beforeInvocation = true)
    public void evictBetExistByEvent(){}

    @Cacheable(value="betsByEventAndOutcome", key="#event.id")
    public ArrayList<Bet> betsByEventAndOutcome(Outcome outcome, Event event){return betRepository.getBetByOutcomeAndEvent(outcome,event);}

    @CacheEvict(value="betsByEventAndOutcome", key="#id",beforeInvocation = true)
    public void evictBetsByEventAndOutcomeByEvent(Long id){}

    @CacheEvict(value="betsByEventAndOutcome", allEntries = true,beforeInvocation = true)
    public void evictBetsByEventAndOutcome(){}
}
