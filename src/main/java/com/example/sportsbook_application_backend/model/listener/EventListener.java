package com.example.sportsbook_application_backend.model.listener;

import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.service.BeanUtil;
import com.example.sportsbook_application_backend.service.BetService;
import com.example.sportsbook_application_backend.service.SlipService;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    Logger logger = LoggerFactory.getLogger(EventListener.class);

    @PostUpdate
    private void afterEventIsFinished(Event event) {
        if (event.getStatus().equals("Match Finished"))
        {
            BeanUtil.getBean(BetService.class).resolveBets(event);
            BeanUtil.getBean(SlipService.class).resolveSlips();
            logger.info("Bets and slips resolved.");
        }
    }
}