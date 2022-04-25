package com.schedule.user.service.jwt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schedule.user.model.UserClaims;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExtractClaimsServiceImpl implements ExtractClaimsService {
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public UserClaims extract(String token) {
        String json = new String(Base64.getDecoder().decode(token.split("\\.")[1]), StandardCharsets.UTF_8);
        Map<String, String> map = objectMapper.readValue(json, new TypeReference<>() {
        });
        return new UserClaims(
                Long.parseLong(map.get("iss")),
                map.get("login")
        );
        //.parseClaimsJwt(token)
        //.parseClaimsJws(token)
    }
}
