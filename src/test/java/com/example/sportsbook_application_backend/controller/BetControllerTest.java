package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.entity.Bet;
import com.example.sportsbook_application_backend.model.entity.Event;
import com.example.sportsbook_application_backend.model.entity.League;
import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.Outcome;
import com.example.sportsbook_application_backend.model.enums.ResultType;
import com.example.sportsbook_application_backend.model.enums.Role;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.repository.*;
import com.example.sportsbook_application_backend.service.BetService;
import com.example.sportsbook_application_backend.service.EventService;
import com.example.sportsbook_application_backend.service.SlipService;
import com.example.sportsbook_application_backend.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)

class BetControllerTest {

    private MockMvc mvc;
    @Autowired private WebApplicationContext context;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;
    @Autowired private BetService betService;
    @Autowired private BetRepository betRepository;
    @MockBean private EventService eventService;
    @Autowired private EventRepository eventRepository;
    @Autowired private LeagueRepository leagueRepository;
    @Autowired private SlipService slipService;
    @Autowired private UserService userService;

    @BeforeEach
    void setUp() {
        userService.evictAllCache();
        slipService.evictHistory();
        betService.evictAllCaches();
        eventService.evictAllCaches();
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        User user = User.builder()
                .userId(1L)
                .firstName("Georgi")
                .lastName("Ivanov")
                .email("gosho@abv.bg")
                .username("gosho123")
                .password(passwordEncoder.encode("Az$um_GOSHO123"))
                .balance(200F)
                .status(UserStatus.ACTIVE)
                .role(Role.USER)
                .build();
        userRepository.save(user);

        League league = new League(1L, "Premier league", "England", "League", 2022, true);
        leagueRepository.save(league);
        Event event = new Event(1L,league, LocalDateTime.now(), LocalDate.now(),"Chelsea","Arsenal","Match Finished", ResultType.TWO);
        eventRepository.save(event);

        Bet bet = new Bet(1L,event, Outcome.PENDING, ResultType.TWO,3.45F);
        betRepository.save(bet);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void placeBet() throws Exception {

        MvcResult login = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"username\": \"gosho123\",\n" +
                        "  \"password\": \"Az$um_GOSHO123\"\n" +
                        "}")).andReturn();
        assertEquals(200,login.getResponse().getStatus());

        MvcResult placeBet1=mvc.perform(post("/user/bet/placeBet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("betId","-11")//negative id
                        .param("stake","30")
                        .header("Authorization",login.getResponse().getHeader("Authorization"))
                        .characterEncoding("UTF-8"))
                        .andReturn();
        assertEquals(400,placeBet1.getResponse().getStatus());
        userService.evictAllCache();
        slipService.evictHistory();
        betService.evictAllCaches();
        eventService.evictAllCaches();
        MvcResult placeBet2=mvc.perform(post("/user/bet/placeBet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("betId","1")//positive existing id
                        .param("stake","30")
                        .header("Authorization",login.getResponse().getHeader("Authorization"))
                        .characterEncoding("UTF-8"))
                        .andReturn();
        assertEquals(200,placeBet2.getResponse().getStatus());
        assertEquals(placeBet2.getResponse().getContentAsString(),"A slip was created successfully.");

    }

    @Test
    void getBetHistory() throws Exception {
        MvcResult login = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"username\": \"gosho123\",\n" +
                        "  \"password\": \"Az$um_GOSHO123\"\n" +
                        "}")).andReturn();
        assertEquals(200,login.getResponse().getStatus());

        MvcResult getBetHistory1=mvc.perform(get("/user/bet/getBetHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",login.getResponse().getHeader("Authorization"))
                        .characterEncoding("UTF-8"))
                .andReturn();
        assertEquals(400,getBetHistory1.getResponse().getStatus());
    }

    @Test
    void getEvents() throws Exception {
        MvcResult login = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"username\": \"gosho123\",\n" +
                        "  \"password\": \"Az$um_GOSHO123\"\n" +
                        "}")).andReturn();
        assertEquals(200,login.getResponse().getStatus());

        Mockito.when(eventService.getAllFixturesByDate("2023-04-01"))
                .thenReturn(new ArrayList<>());

        MvcResult getBetHistory1=mvc.perform(get("/user/bet/getEvents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("date", "2023-04-01")
                        .header("Authorization",login.getResponse().getHeader("Authorization"))
                        .characterEncoding("UTF-8"))
                .andReturn();
        assertEquals(200,getBetHistory1.getResponse().getStatus());
    }
}