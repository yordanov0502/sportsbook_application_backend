package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.exception.NonexistentDataException;
import com.example.sportsbook_application_backend.model.dto.odd.OddDTO;
import com.example.sportsbook_application_backend.model.dto.odd.OddResultDTO;
import com.example.sportsbook_application_backend.model.entity.*;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import com.example.sportsbook_application_backend.repository.BetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BetService {

    private final BetRepository betRepository;
    private final EventService eventService;
    private final RestTemplate restTemplate;

    public int callAPIForOddsByDate(String date) {
        int numberOfOdds=0;

        ArrayList<Event> events = eventService.getAllFixturesByDate(date);

        if(events.isEmpty()) throw new NonexistentDataException("No fixtures found in database for date:"+date);

        //map for storing id and season only of ACTIVE leagues for a specific date
        Map<Long,Integer> activeLeagues = new HashMap<>();
        for(Event event: events)
        {
            activeLeagues.put(event.getLeague().getId(),event.getLeague().getSeason());
        }

        for(Map.Entry<Long,Integer> l: activeLeagues.entrySet())
        {
            //adding all odds for specific date and active league into one list
            List<OddDTO> oddDTOList = restTemplate.getForObject("/odds?league=" + l.getKey() + "&season=" + l.getValue() + "&date=" + date + "&bookmaker=8&bet=1", OddResultDTO.class).getResponse();
            for (OddDTO oddDTO : oddDTOList)
            {
                if(eventService.fixtureExists(oddDTO.getFixture().getId()))
                {
                    Event event = eventService.getFixtureById(oddDTO.getFixture().getId());

                    if(!betRepository.existsBetByEvent(event))
                    {
                        Bet betHome = new Bet(null, event, Outcome.PENDING, ResultType.ONE, Float.parseFloat(oddDTO.getBookmakers().get(0).getBets().get(0).getValues().get(0).getOdd()));
                        Bet betDraw = new Bet(null, event, Outcome.PENDING, ResultType.ZERO, Float.parseFloat(oddDTO.getBookmakers().get(0).getBets().get(0).getValues().get(1).getOdd()));
                        Bet betAway = new Bet(null, event, Outcome.PENDING, ResultType.TWO, Float.parseFloat(oddDTO.getBookmakers().get(0).getBets().get(0).getValues().get(2).getOdd()));

                        betRepository.save(betHome);
                        betRepository.save(betDraw);
                        betRepository.save(betAway);

                        numberOfOdds+=3;
                    }
                }
            }
        }
        return numberOfOdds;
    }

    public int resolveBets(String  date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);

        int resolvedBets=0;
        ArrayList<Bet> bets = betRepository.getBetByOutcome(Outcome.PENDING);
        for (Bet bet:bets) {
            if (parsedDate.equals(bet.getEvent().getDate())) {
                if(Objects.equals(bet.getEvent().getStatus(), "Match Finished")) {
                    if (bet.getType() == bet.getEvent().getResult()) {
                        bet.setOutcome(Outcome.WON);
                    } else {
                        bet.setOutcome(Outcome.LOST);
                    }
                    betRepository.save(bet);
                    resolvedBets++;
                }
            }
        }
        return resolvedBets;
    }

    public boolean isBetExists(Long id){return betRepository.existsById(id);}

    public Bet getBetById(Long id){return betRepository.getBetById(id);}
}