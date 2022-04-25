package com.schedule.user.controller;

import com.schedule.user.model.dto.UserDTO;
import com.schedule.user.model.entity.User;
import com.schedule.user.model.response.GetUserResponse;
import com.schedule.user.service.GetUserByIdService;
import com.schedule.user.service.jwt.ExtractClaimsService;
import com.schedule.user.service.request.ExtractTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/me")
@RequiredArgsConstructor
public class GetMeController {
    private final ExtractTokenService extractTokenService;
    private final ExtractClaimsService extractClaimsService;
    private final GetUserByIdService getUserByIdService;

    @GetMapping
    public ResponseEntity<GetUserResponse> get(HttpServletRequest request) {
        User user = getUserByIdService.get(
                extractClaimsService.extract(
                        extractTokenService.extract(
                                request
                        )
                ).getId()
        );
        return ResponseEntity.ok(
                new GetUserResponse(
                        new UserDTO(
                                user.getLogin(),
                                user.getCreationDate(),
                                user.getEmail(),
                                user.isConfirmed()
                        )
                )
        );
    }
}
