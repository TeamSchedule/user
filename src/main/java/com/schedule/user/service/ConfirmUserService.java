package com.schedule.user.service;

import com.schedule.user.model.entity.User;

import javax.transaction.Transactional;

public interface ConfirmUserService {
    @Transactional
    void confirm(User user);
}
