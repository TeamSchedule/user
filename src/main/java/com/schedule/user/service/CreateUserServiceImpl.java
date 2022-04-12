package com.schedule.user.service;

import com.schedule.user.model.entity.User;
import com.schedule.user.model.request.CreateUserRequest;
import com.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateUserServiceImpl implements CreateUserService {
    private final UserRepository userRepository;

    @Override
    public User create(CreateUserRequest createUserRequest) {
        return userRepository.save(
                new User(
                        createUserRequest.getLogin(),
                        createUserRequest.getPassword(),
                        LocalDateTime.now(),
                        createUserRequest.getEmail()
                )
        );
    }
}