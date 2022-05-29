package com.schedule.user.service;

import com.schedule.user.model.entity.User;
import com.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchUserServiceImpl implements SearchUserService {
    private final UserRepository userRepository;

    @Override
    public List<User> search(String login) {
        return userRepository.searchByNameContains(login);
    }
}
