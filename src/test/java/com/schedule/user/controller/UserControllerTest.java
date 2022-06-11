package com.schedule.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.user.IntegrationTest;
import com.schedule.user.model.dto.UserDTO;
import com.schedule.user.model.entity.User;
import com.schedule.user.model.request.CreateUserRequest;
import com.schedule.user.model.response.CreateUserResponse;
import com.schedule.user.model.response.GetUserResponse;
import com.schedule.user.repository.UserRepository;
import com.schedule.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.temporal.ChronoUnit;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserControllerTest extends IntegrationTest {
    private final MockMvc mockMvc;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Value("${app.jwt.token.headerName}")
    private String tokenHeaderName;
    @Value("${app.jwt.token.test}")
    private String tokenValue;

    @Autowired
    public UserControllerTest(
            MockMvc mockMvc,
            UserService userService,
            ObjectMapper objectMapper,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository
    ) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @AfterEach
    public void afterEach() {
        clearDb();
    }

    @Test
    void registerUserTest() throws Exception {
        String login = "login";
        String password = "password";
        String email = "email@gmail.com";
        CreateUserRequest createTaskRequest = new CreateUserRequest(
                login, password, email
        );
        String requestBody = objectMapper.writeValueAsString(createTaskRequest);

        String response = mockMvc
                .perform(
                        post("/user/")
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CreateUserResponse createUserResponse = objectMapper.readValue(response, CreateUserResponse.class);
        User user = userRepository.findById(createUserResponse.getUserId()).get();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(login, user.getLogin());
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertTrue(passwordEncoder.matches(password, user.getPassword()));
    }

    @Test
    void getMeTest() throws Exception {
        String login = "login";
        String password = "password";
        String email = "email@gmail.com";
        User user = userService.create(login, password, email);

        String response = mockMvc
                .perform(
                        get("/user/me")
                                .header(tokenHeaderName, tokenValue)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GetUserResponse getUserResponse = objectMapper.readValue(response, GetUserResponse.class);
        UserDTO responseUser = getUserResponse.getUser();

        Assertions.assertEquals(user.getId(), responseUser.getId());
        Assertions.assertEquals(user.getLogin(), responseUser.getLogin());
        Assertions.assertEquals(user.getEmail(), responseUser.getEmail());
        Assertions.assertEquals(
                user.getCreationDate().truncatedTo(ChronoUnit.SECONDS),
                responseUser.getCreationDate().truncatedTo(ChronoUnit.SECONDS)
        );
    }
}
