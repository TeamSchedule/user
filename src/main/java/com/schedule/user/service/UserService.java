package com.schedule.user.service;

import com.schedule.user.model.entity.User;

import javax.transaction.Transactional;
import java.util.List;

public interface UserService {
    boolean existsByLogin(String login);

    List<User> searchByLoginContains(String login);

    User getByLogin(String login);

    User getById(Long id);

    User create(
            String login,
            String password,
            String email
    );

    @Transactional
    void confirm(Long id);
}
