package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.Slip;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.repository.SlipRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class SlipCacheService {
    private final SlipRepository slipRepository;

    @Cacheable(value="BetHistory", key="#user.userId")
    public ArrayList<Slip> getHistory(User user){return slipRepository.getExpiredBetsOfUser(user);}

    @CachePut(value="BetHistory", key="#user.userId")
    public ArrayList<Slip> updateHistory(User user){return slipRepository.getExpiredBetsOfUser(user);}

    @CacheEvict(value="BetHistory", allEntries = true,beforeInvocation = true)
    public void evictHistory(){}
}
