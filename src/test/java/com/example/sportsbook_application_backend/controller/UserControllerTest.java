package com.example.sportsbook_application_backend.controller;

import com.example.sportsbook_application_backend.model.entity.User;
import com.example.sportsbook_application_backend.model.enums.Role;
import com.example.sportsbook_application_backend.model.enums.UserStatus;
import com.example.sportsbook_application_backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
class UserControllerTest {

    private MockMvc mvc;
    @Autowired private WebApplicationContext context;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;


    @BeforeEach
    void setUp() {
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
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void userRegistration() throws Exception {

        MvcResult registration1 = mvc.perform(post("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"firstName\": \"Georgi\",\n" +
                        "  \"lastName\": \"Ivanov\",\n" +
                        "  \"email\": \"gosho@abv.bg\",\n" +
                        "  \"username\": \"gosho123\",\n" +
                        "  \"password\": \"Az$um_GOSHO123\"\n" +
                        "}")).andReturn();
        assertEquals(400,registration1.getResponse().getStatus());

        MvcResult registration2 = mvc.perform(post("/user/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"firstName\": \"Ivan\",\n" +
                        "  \"lastName\": \"Petrov\",\n" +
                        "  \"email\": \"ivan@abv.bg\",\n" +
                        "  \"username\": \"ivan123\",\n" +
                        "  \"password\": \"B0502HTto$hko\"\n" +
                        "}")).andReturn();
        assertEquals(200,registration2.getResponse().getStatus());
    }

    @Test
    void userLogin() throws Exception {

        MvcResult login1 = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"username\": \"gosho123\",\n" +
                        "  \"password\": \"Az$um_GOSHO123\"\n" +
                        "}")).andReturn();

        assertEquals(200,login1.getResponse().getStatus());

        MvcResult login2 = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"username\": \"petar123\",\n" +
                        "  \"password\": \"Az$um_PET@R123\"\n" +
                        "}")).andReturn();

        assertEquals(401,login2.getResponse().getStatus());
    }

    @Test
    void getAccount() throws Exception {
        MvcResult login = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"username\": \"gosho123\",\n" +
                        "  \"password\": \"Az$um_GOSHO123\"\n" +
                        "}")).andReturn();

        assertEquals(200,login.getResponse().getStatus());

        MockHttpServletRequestBuilder getAccount =
                MockMvcRequestBuilders.get("/user")
                        .header("Authorization",login.getResponse().getHeader("Authorization"));

        mvc.perform(getAccount)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void editInformation() throws Exception {

        MvcResult login = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"username\": \"gosho123\",\n" +
                        "  \"password\": \"Az$um_GOSHO123\"\n" +
                        "}")).andReturn();

        assertEquals(200,login.getResponse().getStatus());

        MockHttpServletRequestBuilder editInfo =
                MockMvcRequestBuilders.put("/user/edit/information")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization",login.getResponse().getHeader("Authorization"))
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content("{\n" +
                                "  \"id\": \"1\",\n" +
                                "  \"firstName\": \"Georgi\",\n" +
                                "  \"lastName\": \"Ivanov\",\n" +
                                "  \"email\": \"gosho123@abv.bg\",\n" +
                                "  \"username\": \"gosho123\"\n" +
                                "}");

        mvc.perform(editInfo)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        assertEquals("gosho123@abv.bg",userRepository.findUserByUserId(1L).getEmail());
    }

    @Test
    void changePassword() throws Exception {

        MvcResult login = mvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"username\": \"gosho123\",\n" +
                        "  \"password\": \"Az$um_GOSHO123\"\n" +
                        "}")).andReturn();

        assertEquals(200,login.getResponse().getStatus());

        MockHttpServletRequestBuilder editPassword =
                MockMvcRequestBuilders.put("/user/edit/password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization",login.getResponse().getHeader("Authorization"))
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content("{\n" +
                                "  \"id\": \"1\",\n" +
                                "  \"oldPassword\": \"Az$um_GOSHO123\",\n" +
                                "  \"newPassword\": \"Az$um_GOSHO12345\"\n" +
                                "}");

        mvc.perform(editPassword)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        assertTrue(passwordEncoder.matches("Az$um_GOSHO12345",userRepository.findUserByUserId(1L).getPassword()));

    }
}