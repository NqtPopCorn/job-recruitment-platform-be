package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.auth.LoginRequest;
import com.popcorn.jrp.domain.request.auth.RegisterRequest;
import com.popcorn.jrp.domain.response.auth.AccountResponse;

public interface AuthService {
    public String login(LoginRequest request);
    public void register(RegisterRequest request);
    public AccountResponse getAccount(String userId);
}
