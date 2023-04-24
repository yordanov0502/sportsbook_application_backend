package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.exception.NonexistentDataException;
import com.example.sportsbook_application_backend.exception.UpdateException;
import com.example.sportsbook_application_backend.model.dto.league.LeaguesDTO;
import com.example.sportsbook_application_backend.model.dto.league.LeaguesResponseDTO;
import com.example.sportsbook_application_backend.model.entity.League;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {
    private final LeagueCacheService leagueCacheService;
    private final RestTemplate restTemplate;

    public League getLeagueById(Long id){
        return leagueCacheService.getLeagueById(id);
    }

    public void checkForExistingLeagueId(Long leagueId){
        if(leagueCacheService.getLeagueById(leagueId)==null)
            throw new NonexistentDataException("League with id:"+leagueId+", does NOT exist in the database.");
    }

    public int callAPIForLeagues(){
        if(leagueCacheService.getLeagues().size()==0) {
            int number = 0;
            String[] countries = {"World", "Bulgaria", "England", "Spain", "France", "Italia", "Germany"};
            List<String> countriesList = List.of(countries);

            Long[] leagues = {1L, 2L, 3L, 4L, 5L, 9L, 10L, 39L, 40L, 45L, 48L, 61L, 62L, 65L, 66L, 78L, 79L, 81L, 140L, 141L, 143L, 172L, 173L, 174L};
            List<Long> leaguesList = List.of(leagues);

            LeaguesResponseDTO leaguesResponse = restTemplate.getForObject("/leagues", LeaguesResponseDTO.class);

            for (LeaguesDTO leaguesDTO : leaguesResponse.getResponse()) {
                if (countriesList.contains(leaguesDTO.getCountry().getName())) {
                    League league = new League(leaguesDTO.getLeague().getId(), leaguesDTO.getLeague().getName(), leaguesDTO.getCountry().getName(), leaguesDTO.getLeague().getType(), leaguesDTO.getSeasons().get(leaguesDTO.getSeasons().size() - 1).getYear(), false);

                    if (leaguesList.contains(leaguesDTO.getLeague().getId())) {
                        league.setAllowed(true);
                    }
                    number++;
                    leagueCacheService.update(league);
                }
            }
            if(number!=0){
                leagueCacheService.evictLeagues();
            }
            return number;
        }
        else
            throw new UpdateException("The leagues have been already added");
    }

    public String allowLeague(Long id){
        if(getAllowedLeagues().size()<28) {
            League league = getLeagueById(id);
            if(league.isAllowed())
                throw new UpdateException("The league is already allowed");
            else {
                league.setAllowed(true);
                leagueCacheService.update(league);
                leagueCacheService.evictAllowedLeagues();
                return league.getLeague();
            }
        }else {
            throw new UpdateException("The limit of 28 allowed leagues is reached");
        }
    }

    public String  disallowLeague(Long id){
        League league = getLeagueById(id);
        if(league.isAllowed()){
            league.setAllowed(false);
            leagueCacheService.update(league);
            leagueCacheService.evictAllowedLeagues();
            return league.getLeague();
        }
        else
            throw new UpdateException("The league is already disallowed");

    }

    public ArrayList<League> getAllowedLeagues(){
        return leagueCacheService.getAllowedLeagues();
    }

    public List<League> getLeagues(){
        return leagueCacheService.getLeagues();
    }

    public void evictAllCaches() {
        leagueCacheService.evictLeague();
        leagueCacheService.evictLeagues();
        leagueCacheService.evictAllowedLeagues();
    }

}
