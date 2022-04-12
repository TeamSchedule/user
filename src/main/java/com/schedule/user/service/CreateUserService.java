package com.schedule.user.service;

import com.schedule.user.model.entity.User;
import com.schedule.user.model.request.CreateUserRequest;

public interface CreateUserService {
    User create(CreateUserRequest createUserRequest);
}
