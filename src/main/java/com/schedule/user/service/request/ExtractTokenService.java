package com.schedule.user.service.request;

import javax.servlet.http.HttpServletRequest;

public interface ExtractTokenService {
    String extract(HttpServletRequest request);
}
