package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.auth.LoginRequest;
import com.popcorn.jrp.domain.request.auth.RegisterRequest;
import com.popcorn.jrp.domain.response.auth.AccountResponse;
import com.popcorn.jrp.domain.response.ApiResponse;
import com.popcorn.jrp.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.builder()
                        .success(true)
                        .statusCode(HttpStatus.CREATED.value())
                        .message("User registered successfully")
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        String accessToken = authService.login(request);

        // Tạo cookie với accessToken
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Chỉ gửi qua HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(15 * 60); // 15 phút
        response.addCookie(cookie);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Login successful")
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        // Xóa cookie
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .statusCode(HttpStatus.OK.value())
                        .message("Logged out successfully")
                        .build()
        );
    }

//    @GetMapping("/account")
//    public ResponseEntity<AccountResponse> getAccount(Authentication authentication) {
//        String userId = authentication.getName(); // Lấy ID từ JWT
//        AccountResponse account = authService.getAccount(userId);
//        return ResponseEntity.ok(account);
//    }
}