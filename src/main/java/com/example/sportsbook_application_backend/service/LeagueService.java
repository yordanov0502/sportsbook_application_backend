package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.exception.UpdateException;
import com.example.sportsbook_application_backend.model.dto.league.LeaguesDTO;
import com.example.sportsbook_application_backend.model.dto.league.LeaguesResponseDTO;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.repository.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class LeagueService {
    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private RestTemplate restTemplate;

    public League getLeagueById(Long id){
        return leagueRepository.getLeagueById(id);
    }

    public boolean checkIfLeagueIsAllowed(Long id){
        League league=leagueRepository.getLeagueById(id);
        return league.isAllowed();
    }

    public void callAPIForLeagues(){
        LeaguesResponseDTO leaguesResponse=restTemplate.getForObject("/leagues", LeaguesResponseDTO.class);
        for (LeaguesDTO leaguesDTO:leaguesResponse.getResponse()){
            if((Objects.equals(leaguesDTO.getCountry().getName(), "World")||Objects.equals(leaguesDTO.getCountry().getName(), "Bulgaria")||Objects.equals(leaguesDTO.getCountry().getName(), "England")||
                    Objects.equals(leaguesDTO.getCountry().getName(), "Spain")||Objects.equals(leaguesDTO.getCountry().getName(), "France")||Objects.equals(leaguesDTO.getCountry().getName(), "Italia")||
                    Objects.equals(leaguesDTO.getCountry().getName(), "Germany"))&&leaguesDTO.getLeague().getId()<180) {
                League league = new League(leaguesDTO.getLeague().getId(), leaguesDTO.getLeague().getName(), leaguesDTO.getCountry().getName(), leaguesDTO.getLeague().getType(), false);

                //Not sure if there is a better way of comparing the id to exact numbers
                if (league.getId() == 1 || league.getId() == 2 || league.getId() == 3 || league.getId() == 4 || league.getId() == 5 || league.getId() == 9 || league.getId() == 10 || league.getId() == 39 || league.getId() == 40 || league.getId() == 45 || league.getId() == 48 || league.getId() == 61 || league.getId() == 62 || league.getId() == 65 || league.getId() == 66 || league.getId() == 78 || league.getId() == 79 || league.getId() == 81 || league.getId() == 140 || league.getId() == 143 || league.getId() == 172 || league.getId() == 173 || league.getId() == 174 || league.getId() == 175 || league.getId() == 176 || league.getId() == 177 || league.getId() == 178) {
                    league.setAllowed(true);
                }

                leagueRepository.save(league);
            }
        }
    }

    public void setAllowedToLeague(Long id){
        if(leagueRepository.countAllByAllowed(true)<30) {
            League league = getLeagueById(id);
            league.setAllowed(true);
            leagueRepository.save(league);
        }else {
            throw new UpdateException("The limit of 30 allowed leagues is reached");
        }
    }

    public void setDisallowedToLeague(Long id){
        League league = getLeagueById(id);
        league.setAllowed(false);
        leagueRepository.save(league);
    }

    public ArrayList<League> getLeagues(){
        return leagueRepository.getAllByAllowed(true);
    }

}
