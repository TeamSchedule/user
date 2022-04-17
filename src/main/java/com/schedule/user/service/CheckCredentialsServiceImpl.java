package com.schedule.user.service;

import com.schedule.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckCredentialsServiceImpl implements CheckCredentialsService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean check(User user, String login, String password) {
        return user.getLogin().equals(login) &&
                passwordEncoder.matches(password, user.getPassword());
    }
}
