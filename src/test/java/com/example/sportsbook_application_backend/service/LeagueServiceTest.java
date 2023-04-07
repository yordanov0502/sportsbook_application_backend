package com.example.sportsbook_application_backend.service;

import com.example.sportsbook_application_backend.exception.NonexistentDataException;
import com.example.sportsbook_application_backend.exception.UpdateException;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.repository.LeagueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class LeagueServiceTest {

    @MockBean
    private LeagueRepository leagueRepository;
    @Autowired
    private LeagueService leagueService;

    @BeforeEach
    public void setUp() {
        League league = new League(1L, "World cup", "World", "Cup", 2022, true);
        League league2 = new League(2L, "Premier league", "England", "League", 2022, true);
        League league3 = new League(3L, "FA cup", "England", "Cup", 2022, true);
        League league4 = new League(4L, "Purva liga", "Serbia", "League", 2022, false);
        League league5 = new League(5L, "Vtora liga", "Serbia", "League", 2022, false);

        ArrayList<League> leagues = new ArrayList<>();
        leagues.add(league);
        leagues.add(league2);
        leagues.add(league3);
        leagues.add(league4);
        leagues.add(league5);

        for (League leagueTest : leagues) {
           Mockito.when(leagueRepository.getLeagueById(leagueTest.getId()))
                   .thenReturn(leagueTest);
        }

        for (League leagueTest : leagues) {
            Mockito.when(leagueRepository.existsLeagueById(leagueTest.getId()))
                   .thenReturn(true);
        }

        Mockito.when(leagueRepository.countAllByAllowed(true))
               .thenReturn(leagues.size());
        ArrayList<League> allowedLeagues=new ArrayList<>();
        for (League league1:leagues){
            if (league1.isAllowed())
                allowedLeagues.add(league1);
        }

        Mockito.when(leagueRepository.getAllByAllowed(true))
                .thenReturn(allowedLeagues);

        Mockito.when(leagueRepository.findAll())
                .thenReturn(leagues);

    }

    @Test
    void WhenValidId_thenLeagueShouldBeGet() {
        League found=leagueService.getLeagueById(1L);
        assertThat(found.getLeague())
                .isEqualTo("World cup");
        assertThat(found.getSeason())
                .isNotEqualTo(2023);
        assertThat(found.getSeason())
                .isNotEqualTo("League");
        found=leagueService.getLeagueById(2L);
        assertThat(found.getCountry())
                .isEqualTo("England");
        assertThat(found.getSeason())
                .isEqualTo(2022);
        assertThat(found.getType())
                .isEqualTo("League");
        assertThat(found.getCountry())
                .isEqualTo("England");
        found=leagueService.getLeagueById(3L);
        assertThat(found.getLeague())
                .isNotEqualTo("World cup");
    }

    @Test
    void checkForExistingLeagueId() {
        assertThrowsExactly(NonexistentDataException.class,()->leagueService.checkForExistingLeagueId(6L));
        assertThatNoException().isThrownBy(()->leagueService.checkForExistingLeagueId(1L));
    }

    @Test
    void allowLeague() {
        assertThatNoException().isThrownBy(()->leagueService.allowLeague(4L));
        String found=leagueService.allowLeague(5L);
        assertThat(found)
                .isEqualTo("Vtora liga");
        assertThat(found)
                .isNotEqualTo("World cup");

        UpdateException exception = assertThrowsExactly(UpdateException.class, () -> leagueService.allowLeague(2L));
        assertEquals(exception.getMessage(),"The league is already allowed");

        Mockito.when(leagueRepository.countAllByAllowed(true))
                .thenReturn(28);

        exception =assertThrowsExactly(UpdateException.class,()->leagueService.allowLeague(4L));
        assertEquals(exception.getMessage(),"The limit of 28 allowed leagues is reached");

    }

    @Test
    void disallowLeague() {
        String found=leagueService.disallowLeague(3L);
        assertThat(found)
                .isEqualTo("FA cup");
        assertThat(found)
                .isNotEqualTo("World cup");

        UpdateException exception = assertThrowsExactly(UpdateException.class, () -> leagueService.disallowLeague(5L));
        assertEquals(exception.getMessage(),"The league is already disallowed");

        assertThatNoException().isThrownBy(()->leagueService.disallowLeague(1L));
    }

    @Test
    void getAllowedLeagues() {
        ArrayList<League> leagues = leagueService.getAllowedLeagues();
        assertEquals(leagues.size(),3);
    }

    @Test
    void getLeagues() {
        List<League> leagues = leagueService.getLeagues();
        assertEquals(leagues.size(),5);
    }
}