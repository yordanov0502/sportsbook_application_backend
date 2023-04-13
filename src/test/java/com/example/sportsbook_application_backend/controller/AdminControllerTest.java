package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.Role;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.repository.LeagueRepository;
import com.example.sportsbook_application_backend.repository.UserRepository;
import com.example.sportsbook_application_backend.service.LeagueService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class AdminControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private LeagueRepository leagueRepository;
    @MockBean
    private LeagueService leagueService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        User user = User.builder()
                .userId(1L)
                .firstName("ivan")
                .lastName("popov")
                .email("asd.@abf.bg")
                .username("admin_01.")
                .password(passwordEncoder.encode("QUnuJqW#w*%AgKmF9AuvF@UahUCEM"))
                .balance(200F)
                .status(UserStatus.ACTIVE)
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);
    }

    private String login() throws Exception {
        mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"username\":\"asd\",\n" +
                        "    \"password\":\"QUnuJqW#w*%AgKmF9AuvF@UahUCEM\"\n" +
                        "}"))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"username\":\"admin\",\n" +
                                "    \"password\":\"QUnuJqW#w*%AgKmF9AuvF@UahUCEM\"\n" +
                                "}"))
                .andExpect(status().isUnauthorized());

        MvcResult mvcResult = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"username\":\"admin_01.\",\n" +
                        "    \"password\":\"QUnuJqW#w*%AgKmF9AuvF@UahUCEM\"\n" +
                        "}")).andReturn();
        assertEquals(mvcResult.getResponse().getStatus(),200);

        return mvcResult.getResponse().getHeader("Authorization");
    }

    @Test
    void simulateFixtures() throws Exception {
        mvc.perform(get("/admin/simulate")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        mvc.perform(post("/admin/simulate")
                        .param("date", "2023-04-01")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization",login()))
                .andExpect(status().isOk());
    }

    @Test
    void allowLeague() throws Exception {
        Mockito.when(leagueRepository.existsLeagueById(1L))
                        .thenReturn(true);
        Mockito.when(leagueService.allowLeague(1L))
                        .thenReturn("World Cup");

        mvc.perform(post("/admin/allowLeague")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization",login()))
                .andExpect(status().isOk());
    }

    @Test
    void disallowLeague() throws Exception {
        Mockito.when(leagueRepository.existsLeagueById(1L))
                .thenReturn(true);
        Mockito.when(leagueService.disallowLeague(1L))
                .thenReturn("World Cup");

        mvc.perform(post("/admin/disallowLeague")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization",login()))
                .andExpect(status().isOk());
    }

    @Test
    void getAllLeagues() throws Exception {
        mvc.perform(get("/admin/getAllLeagues")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization",login()))
                .andExpect(status().isOk());
    }

    @Test
    void getAllowedLeagues() throws Exception {
        mvc.perform(get("/admin/getAllowedLeagues")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization",login()))
                .andExpect(status().isOk());
    }

    @Test
    void getFixturesByDate() throws Exception {
        mvc.perform(get("/admin/getFixtures")
                        .param("date", "2023-04-01")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization",login()))
                .andExpect(status().isOk());
    }
}