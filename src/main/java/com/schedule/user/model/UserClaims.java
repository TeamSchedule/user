package com.schedule.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserClaims {
    private Long id;
    private String login;
}
