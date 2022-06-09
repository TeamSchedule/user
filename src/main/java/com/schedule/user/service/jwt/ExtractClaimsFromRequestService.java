package com.schedule.user.service.jwt;

import com.schedule.user.model.UserClaims;
import com.schedule.user.model.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface ExtractClaimsFromRequestService {
    User extractUser(HttpServletRequest request);
    UserClaims extractClaims(HttpServletRequest request);
}
