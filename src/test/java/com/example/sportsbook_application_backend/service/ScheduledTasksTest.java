package com.example.sportsbook_application_backend.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class ScheduledTasksTest {
    @Autowired
    private ScheduledTasks scheduledTasks;
    @MockBean
    private EventService eventService;
    @MockBean
    private BetService betService;
    @MockBean
    private SlipService slipService;
    @MockBean
    private LeagueService leagueService;


    @Test
    void ScheduleTest(){
        scheduledTasks.getFixtures();
        scheduledTasks.getOdds();
        scheduledTasks.getFixturesByPreviousDay();
        scheduledTasks.evict();
    }

}