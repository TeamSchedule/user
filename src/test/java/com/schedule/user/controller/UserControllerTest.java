package com.schedule.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.user.IntegrationTest;
import com.schedule.user.model.dto.UserDTO;
import com.schedule.user.model.entity.User;
import com.schedule.user.model.request.CheckCredentialsRequest;
import com.schedule.user.model.request.CreateUserRequest;
import com.schedule.user.model.response.*;
import com.schedule.user.repository.UserRepository;
import com.schedule.user.service.BuildUserDtoService;
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
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserControllerTest extends IntegrationTest {
    private final MockMvc mockMvc;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final BuildUserDtoService buildUserDtoService;
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
            UserRepository userRepository,
            BuildUserDtoService buildUserDtoService
    ) {
        this.mockMvc = mockMvc;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.buildUserDtoService = buildUserDtoService;
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

    @Test
    void checkCredentialsUserIsNotConfirmedTest() throws Exception {
        String login = "login";
        String password = "password";
        String email = "email@gmail.com";
        userService.create(login, password, email);

        CheckCredentialsRequest checkCredentialsRequest = new CheckCredentialsRequest(
                login, password
        );
        String requestBody = objectMapper.writeValueAsString(checkCredentialsRequest);

        String response = mockMvc
                .perform(
                        post("/user/credentials")
                                .header(tokenHeaderName, tokenValue)
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DefaultErrorResponse defaultErrorResponse = objectMapper.readValue(response, DefaultErrorResponse.class);

        Assertions.assertEquals(1, defaultErrorResponse.getErrors().size());
        Assertions.assertEquals("User is not confirmed", defaultErrorResponse.getErrors().get(0));
    }

    @Test
    void checkCredentialsOkTest() throws Exception {
        String login = "login";
        String password = "password";
        String email = "email@gmail.com";
        User user = userService.create(login, password, email);
        userService.confirm(user.getId());

        CheckCredentialsRequest checkCredentialsRequest = new CheckCredentialsRequest(
                login, password
        );
        String requestBody = objectMapper.writeValueAsString(checkCredentialsRequest);

        String response = mockMvc
                .perform(
                        post("/user/credentials")
                                .header(tokenHeaderName, tokenValue)
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CheckCredentialsResponse checkCredentialsResponse = objectMapper.readValue(response, CheckCredentialsResponse.class);

        Assertions.assertEquals(user.getId(), checkCredentialsResponse.getUserId());
    }

    @Test
    void checkCredentialsWrongCredentialsTest() throws Exception {
        String login = "login";
        String password = "password";
        String email = "email@gmail.com";
        User user = userService.create(login, password, email);
        userService.confirm(user.getId());

        CheckCredentialsRequest checkCredentialsRequest = new CheckCredentialsRequest(
                "wrong login", "wrong password"
        );
        String requestBody = objectMapper.writeValueAsString(checkCredentialsRequest);

        String response = mockMvc
                .perform(
                        post("/user/credentials")
                                .header(tokenHeaderName, tokenValue)
                                .contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        DefaultErrorResponse defaultErrorResponse = objectMapper.readValue(response, DefaultErrorResponse.class);

        Assertions.assertEquals(1, defaultErrorResponse.getErrors().size());
        Assertions.assertEquals("Incorrect login or password", defaultErrorResponse.getErrors().get(0));
    }

    @Test
    void confirmUserTest() throws Exception {
        User user = userService.create("login", "password", "email");

        mockMvc
                .perform(
                        patch("/user/" + user.getId())
                )
                .andExpect(status().isOk());

        User confirmedUser = userRepository.findById(user.getId()).get();
        Assertions.assertTrue(confirmedUser.isConfirmed());
    }

    @Test
    void getUsersListByIdsTest() throws Exception {
        String login = "login";
        String password = "password";
        String email = "email@gmail.com";
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = userService.create(i + login, password, i + email);
            user.setCreationDate(user.getCreationDate().truncatedTo(ChronoUnit.SECONDS));
            users.add(user);
        }

        String ids = users.stream().map(User::getId).toList().toString();
        ids = ids.substring(1, ids.length() - 1);

        String response = mockMvc
                .perform(
                        get("/user/" + ids)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        SearchUsersResponse searchUsersResponse = objectMapper.readValue(response, SearchUsersResponse.class);

        List<UserDTO> expectedUsers = users.stream().map(buildUserDtoService::build).toList();
        Assertions.assertEquals(expectedUsers, searchUsersResponse.getUsers());
    }
}
