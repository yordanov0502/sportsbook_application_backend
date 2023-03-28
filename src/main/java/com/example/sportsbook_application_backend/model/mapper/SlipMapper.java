package com.example.sportsbook_application_backend.model.mapper;

import com.example.sportsbook_application_backend.model.dto.slip.SlipDTO;
import com.example.sportsbook_application_backend.model.entity.Slip;
import org.springframework.stereotype.Component;

@Component
public class SlipMapper {
    public SlipDTO mapToSlipDTO(Slip slip)
    {
        return SlipDTO.builder()
                .slipId(slip.getId())
                .betId(slip.getBet().getId())
                .league(slip.getBet().getEvent().getLeague().getLeague())
                .country(slip.getBet().getEvent().getLeague().getCountry())
                .type(slip.getBet().getEvent().getLeague().getType())
                .season(slip.getBet().getEvent().getLeague().getSeason())
                .dateTime(slip.getBet().getEvent().getDateTime())
                .homeTeam(slip.getBet().getEvent().getHomeTeam())
                .awayTeam(slip.getBet().getEvent().getAwayTeam())
                .matchStatus(slip.getBet().getEvent().getStatus())
                .matchResult(slip.getBet().getEvent().getResult())
                .betType(slip.getBet().getType())
                .betOutcome(slip.getBet().getOutcome())
                .betOdd(slip.getBet().getOdd())
                .slipStake(slip.getStake())
                .expectedProfit(slip.getExpectedProfit())
                .slipOutcome(slip.getOutcome())
                .build();
    }
}