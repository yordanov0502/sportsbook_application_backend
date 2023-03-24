package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.dto.odd.OddDTO;
import com.example.sportsbook_application_backend.model.dto.odd.OddResultDTO;
import com.example.sportsbook_application_backend.model.entity.Bet;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import com.example.sportsbook_application_backend.repository.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BetService {

    @Autowired
    private BetRepository betRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LeagueService leagueService;

    public void callAPIForOddsByDate(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);

        ArrayList<Event> events = eventService.getAllFixturesByDate(parsedDate);
        Map<Long,OddDTO> odds = new HashMap<>();



        for(League league:leagueService.getLeagues()) {
            //adding all odd responses into one list
            List<OddDTO> oddDTOList = restTemplate.getForObject("/odds?league="+league.getId()+"&season="+league.getSeason()+"&date="+parsedDate+"&bookmaker=8&bet=1",OddResultDTO.class).getResponse();
            for(OddDTO oddDTO: oddDTOList)
            {
                odds.put(oddDTO.getFixture().getId(), oddDTO);
            }
        }



        for(Event event : events)
        {
            if(odds.containsKey(event.getId()))
            {
                Bet betHome = new Bet(null, event, Outcome.PENDING, ResultType.ONE,Float.parseFloat(odds.get(event.getId()).getBookmakers().get(0).getBets().get(0).getValues().get(0).getOdd()));
                Bet betDraw = new Bet(null, event,Outcome.PENDING, ResultType.ZERO,Float.parseFloat(odds.get(event.getId()).getBookmakers().get(0).getBets().get(0).getValues().get(1).getOdd()));
                Bet betAway = new Bet(null, event,Outcome.PENDING, ResultType.TWO,Float.parseFloat(odds.get(event.getId()).getBookmakers().get(0).getBets().get(0).getValues().get(2).getOdd()));

                betRepository.save(betHome);
                betRepository.save(betDraw);
                betRepository.save(betAway);
            }
        }

        }
    }