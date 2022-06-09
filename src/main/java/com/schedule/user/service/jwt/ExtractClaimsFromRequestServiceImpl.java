package com.schedule.user.service.jwt;

import com.schedule.user.model.UserClaims;
import com.schedule.user.model.entity.User;
import com.schedule.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class ExtractClaimsFromRequestServiceImpl implements ExtractClaimsFromRequestService {
    private final ExtractClaimsFromTokenService extractClaimsFromTokenService;
    private final ExtractTokenService extractTokenService;
    private final UserService userService;

    @Override
    public User extractUser(HttpServletRequest request) {
        return userService.getById(extractClaims(request).getId());
    }

    @Override
    public UserClaims extractClaims(HttpServletRequest request) {
        return extractClaimsFromTokenService.extract(
                extractTokenService.extract(
                        request
                )
        );
    }
}
