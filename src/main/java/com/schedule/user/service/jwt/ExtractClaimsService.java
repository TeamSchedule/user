package com.schedule.user.service.jwt;

import io.jsonwebtoken.Claims;

public interface ExtractClaimsService {
    Claims extract(String token);
}
