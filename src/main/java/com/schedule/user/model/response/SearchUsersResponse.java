package com.schedule.user.model.response;

import com.schedule.user.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchUsersResponse {
    private List<UserDTO> users;
}
