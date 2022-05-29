package com.schedule.user.service;

import com.schedule.user.model.entity.User;

import java.util.List;

public interface SearchUserService {
    List<User> search(String login);
}
