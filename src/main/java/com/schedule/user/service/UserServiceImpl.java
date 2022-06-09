package com.schedule.user.service;

import com.schedule.user.model.entity.User;
import com.schedule.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public List<User> searchByLoginContains(String login) {
        return userRepository.searchByNameContains(login);
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.getByLogin(login);
    }

    @Override
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User create(
            String login,
            String password,
            String email
    ) {
        return userRepository.save(
                new User(
                        login,
                        passwordEncoder.encode(password),
                        LocalDateTime.now(),
                        email
                )
        );
    }

    @Override
    @Transactional
    public void confirm(User user) {
        user.setConfirmed(true);
    }
}
