package com.schedule.user.service;

import com.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserExistsByLoginServiceImpl implements UserExistsByLoginService {
    private final UserRepository userRepository;

    @Override
    public boolean exists(String login) {
        return userRepository.existsByLogin(login);
    }
}
