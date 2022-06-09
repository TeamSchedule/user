package com.schedule.user.controller;

import com.schedule.user.model.dto.UserDTO;
import com.schedule.user.model.entity.User;
import com.schedule.user.model.request.CheckCredentialsRequest;
import com.schedule.user.model.request.CreateUserRequest;
import com.schedule.user.model.response.*;
import com.schedule.user.service.*;
import com.schedule.user.service.jwt.ExtractClaimsService;
import com.schedule.user.service.request.ExtractTokenService;
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
    private final CheckCredentialsService checkCredentialsService;
    private final ExtractTokenService extractTokenService;
    private final ExtractClaimsService extractClaimsService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<SearchUsersResponse> search(
            @RequestParam String criteria
    ) {
        return ResponseEntity.ok().body(
                new SearchUsersResponse(
                        userService
                                .searchByLoginContains(criteria)
                                .stream()
                                .map(
                                        // TODO: service
                                        user -> new UserDTO(
                                                user.getId(),
                                                user.getLogin(),
                                                user.getCreationDate(),
                                                user.getEmail(),
                                                user.isConfirmed()
                                        ))
                                .toList()
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserResponse> get(
            HttpServletRequest request
    ) {
        User user = userService.getById(
                extractClaimsService.extract(
                        extractTokenService.extract(
                                request
                        )
                ).getId()
        );
        return ResponseEntity.ok(
                new GetUserResponse(
                        new UserDTO(
                                user.getId(),
                                user.getLogin(),
                                user.getCreationDate(),
                                user.getEmail(),
                                user.isConfirmed()
                        )
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
                userService.getById(
                        userId
                )
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/credentials")
    public ResponseEntity<?> check(@RequestBody CheckCredentialsRequest checkCredentialsRequest) {
        // TODO: validator
        User user = userService.getByLogin(checkCredentialsRequest.getLogin());
        if (!user.isConfirmed()) {
            return ResponseEntity.badRequest().body(
                    new DefaultErrorResponse(
                            1,
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
                            1,
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
