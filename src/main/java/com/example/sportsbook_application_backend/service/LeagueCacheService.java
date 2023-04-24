package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.repository.LeagueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueCacheService {
    private final LeagueRepository leagueRepository;

    @Cacheable(value="league", key="#id")
    public League getLeagueById(Long id){
        return leagueRepository.getLeagueById(id);
    }

    @CachePut(value = "league", key="#league.id")
    public League update(League league){
        return leagueRepository.save(league);
    }

    @CacheEvict(value="league",allEntries = true,beforeInvocation = true)
    public void evictLeague() {
    }

    @Cacheable(value="allowedLeagues")
    public ArrayList<League> getAllowedLeagues(){
        return leagueRepository.getAllByAllowed(true);
    }

    @CacheEvict(value="allowedLeagues", allEntries = true)
    public void evictAllowedLeagues() {
    }

    @Cacheable(value="leagues")
    public List<League> getLeagues(){
        return leagueRepository.findAll();
    }

    @CacheEvict(value="leagues",allEntries = true,beforeInvocation = true)
    public void evictLeagues() {
    }

}
