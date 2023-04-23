package com.example.sportsbook_application_backend.config;

import com.example.sportsbook_application_backend.service.BetService;
import com.example.sportsbook_application_backend.service.EventService;
import com.example.sportsbook_application_backend.service.SlipService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
	private final EventService eventService;
	private final BetService betService;
	private final SlipService slipService;

	@Scheduled(cron = "10 */5 * * * *")
	public void getFixtures() {
		eventService.callAPIForFixtures(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}

	@Scheduled(cron = "0 2 0 * * *")
	public void getOdds() {
		betService.callAPIForOddsByDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}

	@Scheduled(cron = "0 2 2 * * *")
	public void getFixturesByPreviousDay() {
		eventService.callAPIForFixtures(LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}
}