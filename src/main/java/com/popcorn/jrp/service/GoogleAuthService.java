package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.auth.LoginGoogleRequest;
import com.popcorn.jrp.domain.request.auth.RegisterGoogleRequest;

public interface GoogleAuthService {
    String loginWithGoogle(LoginGoogleRequest request);
    String registerWithGoogle(RegisterGoogleRequest request);
}
