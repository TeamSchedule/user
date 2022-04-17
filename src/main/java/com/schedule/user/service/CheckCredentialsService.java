package com.schedule.user.service;

import com.schedule.user.model.entity.User;

public interface CheckCredentialsService {
    boolean check(User user, String login, String password);
}
