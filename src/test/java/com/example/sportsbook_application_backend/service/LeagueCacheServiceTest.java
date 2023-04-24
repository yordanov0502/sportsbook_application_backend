package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.repository.LeagueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class LeagueCacheServiceTest {
    @MockBean
    private LeagueRepository leagueRepository;
    @Autowired
    private LeagueCacheService leagueCacheService;


    @BeforeEach
    public void clear(){
        leagueCacheService.evictAllowedLeagues();
        leagueCacheService.evictLeagues();
        leagueCacheService.evictLeagues();
    }

    @Test
    void leagueTest() {
        League league = new League(1L, "World cup", "World", "Cup", 2022, true);
        Mockito.when(leagueRepository.getLeagueById(league.getId()))
                .thenReturn(league);

        League found = leagueCacheService.getLeagueById(1L);
        assertThat(found.getLeague())
                .isEqualTo("World cup");
        assertThat(found.isAllowed()).isEqualTo(true);
        league.setAllowed(false);
        Mockito.when(leagueRepository.save(league))
                .thenReturn(league);

        leagueCacheService.update(league);
        found = leagueCacheService.getLeagueById(1L);
        assertThat(found.isAllowed()).isEqualTo(false);

        verify(leagueRepository,times(1)).getLeagueById(1L);
        leagueCacheService.evictLeague();
        leagueCacheService.getLeagueById(1L);

        found = leagueCacheService.getLeagueById(1L);
        assertThat(found.isAllowed()).isEqualTo(false);

        verify(leagueRepository,times(2)).getLeagueById(1L);
    }

    @Test
    void getAllowedLeagues() {
        League league = new League(1L, "World cup", "World", "Cup", 2022, true);
        League league2 = new League(2L, "World cup", "World", "Cup", 2022, true);

        ArrayList<League> leagues = new ArrayList<>();
        leagues.add(league);
        leagues.add(league2);

        Mockito.when(leagueRepository.getAllByAllowed(true))
                .thenReturn(leagues);

        ArrayList<League> found = leagueCacheService.getAllowedLeagues();
        assertThat(found.size())
                .isEqualTo(2);

        verify(leagueRepository,times(1)).getAllByAllowed(true);

        League league3 = new League(3L, "World cup", "World", "Cup", 2022, true);
        leagues.add(league3);

        Mockito.when(leagueRepository.getAllByAllowed(true))
                .thenReturn(leagues);

        leagueCacheService.evictAllowedLeagues();

        found = leagueCacheService.getAllowedLeagues();
        assertThat(found.size())
                .isEqualTo(3);

        verify(leagueRepository,times(2)).getAllByAllowed(true);
    }

    @Test
    void getLeagues() {
        League league = new League(1L, "World cup", "World", "Cup", 2022, true);
        League league2 = new League(2L, "World cup", "World", "Cup", 2022, true);

        ArrayList<League> leagues = new ArrayList<>();
        leagues.add(league);
        leagues.add(league2);

        Mockito.when(leagueRepository.findAll())
                .thenReturn(leagues);

        List<League> found = leagueCacheService.getLeagues();
        assertThat(found.size())
                .isEqualTo(2);

        League league3 = new League(3L, "World cup", "World", "Cup", 2022, false);
        leagues.add(league3);

        verify(leagueRepository,times(1)).findAll();

        Mockito.when(leagueRepository.findAll())
                .thenReturn(leagues);

        leagueCacheService.evictLeagues();

        found = leagueCacheService.getLeagues();
        assertThat(found.size())
                .isEqualTo(3);

        verify(leagueRepository,times(2)).findAll();
    }

}