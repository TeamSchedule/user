package com.schedule.user.service.jwt;

import javax.servlet.http.HttpServletRequest;

interface ExtractTokenService {
    String extract(HttpServletRequest request);
}
