package com.schedule.user.service;

import com.schedule.user.model.entity.User;
import com.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserByIdServiceImpl implements GetUserByIdService {
    private final UserRepository userRepository;

    @Override
    public User get(Long id) {
        return userRepository.getById(id);
    }
}
