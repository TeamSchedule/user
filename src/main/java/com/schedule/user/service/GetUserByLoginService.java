package com.schedule.user.service;

import com.schedule.user.model.entity.User;

public interface GetUserByLoginService {
    User get(String login);
}
