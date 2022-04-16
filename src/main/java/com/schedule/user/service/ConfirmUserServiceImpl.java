package com.schedule.user.service;

import com.schedule.user.model.entity.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ConfirmUserServiceImpl implements ConfirmUserService {
    @Override
    @Transactional
    public void confirm(User user) {
        user.setConfirmed(true);
    }
}
