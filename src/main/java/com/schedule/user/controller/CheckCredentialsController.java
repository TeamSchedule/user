package com.schedule.user.controller;

import com.schedule.user.model.entity.User;
import com.schedule.user.model.request.CheckCredentialsRequest;
import com.schedule.user.model.response.CheckCredentialsResponse;
import com.schedule.user.model.response.DefaultErrorResponse;
import com.schedule.user.service.CheckCredentialsService;
import com.schedule.user.service.GetUserByLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/credentials")
public class CheckCredentialsController {
    private final CheckCredentialsService checkCredentialsService;
    private final GetUserByLoginService getUserByLoginService;

    @PostMapping
    public ResponseEntity<?> check(@RequestBody CheckCredentialsRequest checkCredentialsRequest) {
        User user = getUserByLoginService.get(checkCredentialsRequest.getLogin());
        boolean checkCredentials = checkCredentialsService.check(
                user,
                checkCredentialsRequest.getLogin(),
                checkCredentialsRequest.getPassword()
        );
        if(!checkCredentials) {
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
