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

        Long [] leagues = {1L,2L,3L,4L,5L,9L,10L,39L,40L,45L,48L,61L,62L,65L,66L,78L,79L,81L,140L,141L,143L,172L,173L,174L};
        List<Long> leaguesList = List.of(leagues);

        LeaguesResponseDTO leaguesResponse=restTemplate.getForObject("/leagues", LeaguesResponseDTO.class);

        for (LeaguesDTO leaguesDTO:leaguesResponse.getResponse()){
            if(countriesList.contains(leaguesDTO.getCountry().getName())/*&&leaguesDTO.getLeague().getId()<180*/) {
                League league = new League(leaguesDTO.getLeague().getId(), leaguesDTO.getLeague().getName(), leaguesDTO.getCountry().getName(), leaguesDTO.getLeague().getType(),leaguesDTO.getSeasons().get(leaguesDTO.getSeasons().size()-1).getYear(), false);

                if(leaguesList.contains(leaguesDTO.getLeague().getId())){
                    league.setAllowed(true);
                }

                leagueRepository.save(league);
            }
        }
    }

    public String allowLeague(Long id){
        if(leagueRepository.countAllByAllowed(true)<28) {
            League league = getLeagueById(id);
            league.setAllowed(true);
            leagueRepository.save(league);
            return league.getLeague();
        }else {
            throw new UpdateException("The limit of 28 allowed leagues is reached");
        }
    }

    public String  disallowLeague(Long id){
        League league = getLeagueById(id);
        league.setAllowed(false);
        leagueRepository.save(league);
        return league.getLeague();
    }

    public ArrayList<League> getAllowedLeagues(){
        return leagueRepository.getAllByAllowed(true);
    }

    public List<League> getLeagues(){
        return leagueRepository.findAll();
    }

}
