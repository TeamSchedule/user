package com.schedule.user.controller;

import com.schedule.user.service.ConfirmUserService;
import com.schedule.user.service.GetUserByIdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class ConfirmUserController {
    private final GetUserByIdService getUserByIdService;
    private final ConfirmUserService confirmUserService;

    @PatchMapping("/{userId}")
    public ResponseEntity<?> confirm(@PathVariable Long userId) {
        confirmUserService.confirm(
                getUserByIdService.get(
                        userId
                )
        );
        return ResponseEntity.ok().build();
    }
}
