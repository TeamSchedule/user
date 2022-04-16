package com.schedule.user.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "app_user")
@Data
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "email")
    private String email;

    @Column(name = "confirmed")
    private boolean confirmed;

    public User(
            String login,
            String password,
            LocalDateTime creationDate,
            String email
    ) {
        this.login = login;
        this.password = password;
        this.creationDate = creationDate;
        this.email = email;

        this.confirmed = false;
    }
}
