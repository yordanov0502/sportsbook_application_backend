package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.Role;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.repository.UserRepository;
import com.example.sportsbook_application_backend.service.BetService;
import com.example.sportsbook_application_backend.service.EventService;
import com.example.sportsbook_application_backend.service.LeagueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class ExternalAPIControllerTest {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private BetService betService;
    @MockBean
    private EventService eventService;
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

    @Test
    @Order(1)
    void getLeagues() throws Exception {
        MvcResult login = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"username\":\"admin_01.\",\n" +
                        "    \"password\":\"QUnuJqW#w*%AgKmF9AuvF@UahUCEM\"\n" +
                        "}")).andReturn();

        mvc.perform(get("/admin/external-api/getLeagues")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        assertEquals(login.getResponse().getStatus(),200);

        MvcResult mvcResult=mvc.perform(get("/admin/external-api/getLeagues")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization",login.getResponse().getHeader("Authorization")))
                .andReturn();
        assertEquals(mvcResult.getResponse().getStatus(),200);
        assertEquals(mvcResult.getResponse().getContentAsString(),"0 leagues have been added to the database successfully.");
    }

    @Test
    @Order(2)
    void getFixtures() throws Exception {
        MvcResult login = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"username\":\"admin_01.\",\n" +
                        "    \"password\":\"QUnuJqW#w*%AgKmF9AuvF@UahUCEM\"\n" +
                        "}")).andReturn();

        assertEquals(login.getResponse().getStatus(),200);

        MvcResult mvcResult=mvc.perform(get("/admin/external-api/getFixtures")
                        .param("date", "2023-04-01")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization",login.getResponse().getHeader("Authorization")))
                .andReturn();
        assertEquals(mvcResult.getResponse().getStatus(),400);
        assertEquals(mvcResult.getResponse().getContentAsString(),"NONE fixtures have been added to the database!");

        Mockito.when(eventService.callAPIForFixtures("2023-04-01"))
                .thenReturn(1);

        mvcResult=mvc.perform(get("/admin/external-api/getFixtures")
                        .param("date", "2023-04-01")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization",login.getResponse().getHeader("Authorization")))
                .andReturn();
        assertEquals(mvcResult.getResponse().getStatus(),200);
        assertEquals(mvcResult.getResponse().getContentAsString(),"1 fixtures have been added to the database.");
    }

    @Test
    @Order(3)
    void getOdds() throws Exception {
        MvcResult login = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"username\":\"admin_01.\",\n" +
                        "    \"password\":\"QUnuJqW#w*%AgKmF9AuvF@UahUCEM\"\n" +
                        "}")).andReturn();

        assertEquals(login.getResponse().getStatus(),200);

        MvcResult mvcResult=mvc.perform(get("/admin/external-api/getOdds")
                        .param("date", "2023-04-01")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization",login.getResponse().getHeader("Authorization")))
                .andReturn();
        assertEquals(mvcResult.getResponse().getStatus(),200);
        assertEquals(mvcResult.getResponse().getContentAsString(),"Odds have been added to 0 fixtures.");
    }
}