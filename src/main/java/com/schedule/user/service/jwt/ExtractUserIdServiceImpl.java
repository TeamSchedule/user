package com.schedule.user.service.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExtractUserIdServiceImpl implements ExtractUserIdService {
    private final ExtractClaimsService extractClaimsService;

    @Override
    public Long extract(String token) {
        return Long.parseLong(extractClaimsService.extract(token).getIssuer());
    }
}
