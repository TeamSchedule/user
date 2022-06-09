package com.schedule.user.service.jwt;


import com.schedule.user.model.UserClaims;

interface ExtractClaimsFromTokenService {
    UserClaims extract(String token);
}
