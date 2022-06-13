package com.schedule.user.controller;

import com.schedule.user.model.entity.User;
import com.schedule.user.model.request.CheckCredentialsRequest;
import com.schedule.user.model.request.CreateUserRequest;
import com.schedule.user.model.response.*;
import com.schedule.user.service.BuildUserDtoService;
import com.schedule.user.service.CheckCredentialsService;
import com.schedule.user.service.UserService;
import com.schedule.user.service.jwt.ExtractClaimsFromRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final ExtractClaimsFromRequestService extractClaimsFromRequestService;
    private final CheckCredentialsService checkCredentialsService;
    private final BuildUserDtoService buildUserDtoService;
    private final UserService userService;

    @GetMapping("/{usersIds}")
    public ResponseEntity<SearchUsersResponse> get(
            @PathVariable List<Long> usersIds
    ) {
        return ResponseEntity.ok().body(
                new SearchUsersResponse(
                        userService
                                .getByIds(usersIds)
                                .stream()
                                .map(buildUserDtoService::build)
                                .toList()
                )
        );
    }

    @GetMapping
    public ResponseEntity<SearchUsersResponse> search(
            @RequestParam String criteria
    ) {
        return ResponseEntity.ok().body(
                new SearchUsersResponse(
                        userService
                                .searchByLoginContains(criteria)
                                .stream()
                                .map(buildUserDtoService::build)
                                .toList()
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserResponse> get(
            HttpServletRequest request
    ) {
        User user = extractClaimsFromRequestService.extractUser(request);
        return ResponseEntity.ok(
                new GetUserResponse(
                        buildUserDtoService.build(user)
                )
        );
    }

    @PostMapping
    public ResponseEntity<CreateUserResponse> register(
            @RequestBody @Valid CreateUserRequest createUserRequest
    ) {
        User user = userService.create(
                createUserRequest.getLogin(),
                createUserRequest.getPassword(),
                createUserRequest.getEmail()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new CreateUserResponse(
                                user.getId()
                        )
                );
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> confirm(@PathVariable Long userId) {
        userService.confirm(
                userId
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/credentials")
    public ResponseEntity<?> check(
            @RequestBody CheckCredentialsRequest checkCredentialsRequest,
            HttpServletRequest request
    ) {
        // TODO: validator
        User user = extractClaimsFromRequestService.extractUser(request);
        if (!user.isConfirmed()) {
            return ResponseEntity.badRequest().body(
                    new DefaultErrorResponse(
                            List.of("User is not confirmed")
                    )
            );
        }

        boolean checkCredentials = checkCredentialsService.check(
                user,
                checkCredentialsRequest.getLogin(),
                checkCredentialsRequest.getPassword()
        );
        if (!checkCredentials) {
            return ResponseEntity.badRequest().body(
                    new DefaultErrorResponse(
                            List.of("Incorrect login or password")
                    )
            );
        }

        return ResponseEntity.ok().body(
                new CheckCredentialsResponse(
                        user.getId()
                )
        );
    }

}
