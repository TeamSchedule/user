package com.schedule.user.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CheckCredentialsRequest {
    @NotEmpty(message = "Login is mandatory")
    @Size(min = 3, max = 30, message = "Login length must be in range between 3 and 30")
    private String login;

    @NotEmpty(message = "Password is mandatory")
    @Size(min = 3, max = 30, message = "Password length must be in range between 3 and 30")
    private String password;
}
