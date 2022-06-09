package com.schedule.user.service;

import com.schedule.user.model.dto.UserDTO;
import com.schedule.user.model.entity.User;

public interface BuildUserDtoService {
    UserDTO build(User user);
}
