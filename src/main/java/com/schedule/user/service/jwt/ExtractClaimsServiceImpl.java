package com.schedule.user.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class ExtractClaimsServiceImpl implements ExtractClaimsService {
    @Override
    public Claims extract(String token) {
        return Jwts
                .parser()
                .parseClaimsJws(token)
                .getBody();
    }
}
