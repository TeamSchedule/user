package com.schedule.user.controller;

import com.schedule.user.model.request.CreateUserRequest;
import com.schedule.user.model.response.CreateUserResponse;
import com.schedule.user.service.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class CreateUserController {
    private final CreateUserService createUserService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid CreateUserRequest createUserRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new CreateUserResponse(
                                createUserService.create(createUserRequest).getId()
                        )
                );
    }
}
