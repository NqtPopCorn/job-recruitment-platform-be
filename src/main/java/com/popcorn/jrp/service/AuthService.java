package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.auth.LoginRequest;

public interface AuthService {
    public String login(LoginRequest request);
}
