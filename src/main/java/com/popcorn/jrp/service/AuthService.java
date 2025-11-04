package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.entity.UserEntity;
import com.popcorn.jrp.domain.request.auth.LoginRequest;
import com.popcorn.jrp.domain.request.auth.RegisterRequest;
import com.popcorn.jrp.domain.response.auth.AccountResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    public String login(LoginRequest request);

    public UserEntity register(RegisterRequest request);

    public AccountResponse getAccount(String userId);

    public void addAccessTokenCookie(HttpServletResponse response, String accessToken);

    public void clearAccessTokenCookie(HttpServletRequest request, HttpServletResponse response);
}
