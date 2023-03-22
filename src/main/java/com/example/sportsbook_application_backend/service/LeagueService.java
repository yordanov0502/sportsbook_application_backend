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
import java.util.List;

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
        String [] countries ={"World","Bulgaria","England","Spain","France","Italia","Germany"};
        List<String> countriesList=List.of(countries);

        LeaguesResponseDTO leaguesResponse=restTemplate.getForObject("/leagues", LeaguesResponseDTO.class);

        for (LeaguesDTO leaguesDTO:leaguesResponse.getResponse()){
            if(countriesList.contains(leaguesDTO.getCountry().getName())&&leaguesDTO.getLeague().getId()<180) {
                League league = new League(leaguesDTO.getLeague().getId(), leaguesDTO.getLeague().getName(), leaguesDTO.getCountry().getName(), leaguesDTO.getLeague().getType(), false);

                Long [] leagues = {1L,2L,3L,4L,5L,9L,10L,39L,40L,45L,48L,61L,62L,65L,66L,78L,79L,81L,140L,141L,143L,172L,173L,174L,175L,176L,177L,178L};
                List<Long> leaguesList = List.of(leagues);

                if(leaguesList.contains(leaguesDTO.getLeague().getId())){
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
