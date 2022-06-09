package com.schedule.user.service;

import com.schedule.user.model.dto.UserDTO;
import com.schedule.user.model.entity.User;
import org.springframework.stereotype.Service;

@Service
public class BuildUserDtoServiceImpl implements BuildUserDtoService {
    @Override
    public UserDTO build(User user) {
        return new UserDTO(
                user.getId(),
                user.getLogin(),
                user.getCreationDate(),
                user.getEmail(),
                user.isConfirmed()
        );
    }
}
