package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.dto.odd.OddDTO;
import com.example.sportsbook_application_backend.model.dto.odd.OddResultDTO;
import com.example.sportsbook_application_backend.model.entity.Bet;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.enums.Type;
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

    public void callAPIForOddsByDate(String date){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(/*"2023-03-24"*/date, formatter);

        ArrayList<Event> events = eventService.getAllFixturesByDate(localDate);
        Map<Long,OddDTO> odds = new HashMap<>();

        int pages = restTemplate.getForObject("/odds?date="+localDate+"&bookmaker=6&bet=1",OddResultDTO.class).getPaging().getTotal();//number of odd pages


        //adding all odd responses into one list
        for(int currPage=1;currPage<=pages;currPage++)
        {
            List<OddDTO> oddDTOList = restTemplate.getForObject("/odds?date="+localDate+"&page="+currPage+"&bookmaker=6&bet=1",OddResultDTO.class).getResponse();
            for(OddDTO oddDTO: oddDTOList)
            {
                odds.put(oddDTO.getFixture().getId(), oddDTO);
            }
        }


        for(Event event : events)
        {
            if(odds.containsKey(event.getId()))
            {
                Bet betHome = new Bet(null, event,null, Type.ONE,Float.parseFloat(odds.get(event.getId()).getBookmakers().get(0).getBets().get(0).getValues().get(0).getOdd()));
                Bet betDraw = new Bet(null, event,null, Type.ZERO,Float.parseFloat(odds.get(event.getId()).getBookmakers().get(0).getBets().get(0).getValues().get(1).getOdd()));
                Bet betAway = new Bet(null, event,null, Type.TWO,Float.parseFloat(odds.get(event.getId()).getBookmakers().get(0).getBets().get(0).getValues().get(2).getOdd()));

                betRepository.save(betHome);
                betRepository.save(betDraw);
                betRepository.save(betAway);
            }
        }

        }
    }