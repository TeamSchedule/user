package com.schedule.user.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.user.IntegrationTest;
import com.schedule.user.model.dto.UserDTO;
import com.schedule.user.model.entity.User;
import com.schedule.user.model.response.SearchUsersResponse;
import com.schedule.user.service.BuildUserDtoService;
import com.schedule.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class SearchUsersTest extends IntegrationTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final String uniqueLogin;
    private final List<User> users;
    private final BuildUserDtoService buildUserDtoService;

    @Autowired
    public SearchUsersTest(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            UserService userService,
            BuildUserDtoService buildUserDtoService
    ) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.buildUserDtoService = buildUserDtoService;
        this.uniqueLogin = "unique login";
        this.users = new ArrayList<>();
    }

    @AfterEach
    public void afterEach() {
        clearDb();
    }

    @BeforeEach
    public void beforeEach() {
        User uniqueUser = userService.create(uniqueLogin, "", "");
        uniqueUser.setCreationDate(uniqueUser.getCreationDate().truncatedTo(ChronoUnit.SECONDS));
        users.add(uniqueUser);
        StringBuilder curLogin = new StringBuilder();
        for (char login = 'a'; login <= 'z'; login++) {
            curLogin.append(login);
            User user = userService.create(curLogin.toString(), "", "");
            user.setCreationDate(user.getCreationDate().truncatedTo(ChronoUnit.SECONDS));
            users.add(user);
        }
    }

    @Test
    void searchUniqueLoginTest() throws Exception {
        String response = mockMvc
                .perform(
                        get("/user")
                                .queryParam("criteria", uniqueLogin)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        SearchUsersResponse searchUsersResponse = objectMapper.readValue(response, SearchUsersResponse.class);

        List<UserDTO> expectedUsers = users
                .stream()
                .filter(u -> u.getLogin().equals(uniqueLogin))
                .map(buildUserDtoService::build)
                .toList();
        Assertions.assertEquals(expectedUsers, searchUsersResponse.getUsers());
    }
}
