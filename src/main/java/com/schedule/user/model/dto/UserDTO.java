package com.schedule.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String login;
    private LocalDateTime creationDate;
    private String email;
    private boolean confirmed;
}
