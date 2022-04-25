package com.schedule.user.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class ExtractClaimsServiceImpl implements ExtractClaimsService {
    @Override
    public Claims extract(String token) {
        System.out.println(token);
        return Jwts
                .parser()
                .setSigningKey("kXpBmV^_|BFq#c.-\"\"B:cd#k6-/EuVp]")
                .parseClaimsJws(token)
                .getBody();
    }
}
