package com.schedule.user.service.jwt;

import com.schedule.user.model.UserClaims;

public interface ExtractClaimsService {
    UserClaims extract(String token);
}
