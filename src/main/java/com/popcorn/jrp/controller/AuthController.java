package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.entity.UserEntity;
import com.popcorn.jrp.domain.request.auth.LoginRequest;
import com.popcorn.jrp.domain.request.auth.RegisterRequest;
import com.popcorn.jrp.domain.response.auth.AccountResponse;
import com.popcorn.jrp.domain.response.auth.UserResponse;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiResponse;
import com.popcorn.jrp.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
        @ResponseStatus(HttpStatus.CREATED)
        public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
                UserEntity user = authService.register(request);
                UserResponse userDto = UserResponse.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .role(user.getRole().name())
                                .createdAt(user.getCreatedAt())
                                .updatedAt(user.getUpdatedAt())
                                .build();

                return ApiResponse.<UserResponse>builder()
                                .success(true)
                                .statusCode(HttpStatus.CREATED.value())
                                .message("User registered successfully")
                                .data(userDto)
                                .build();
        }

        @PostMapping("/login")
        @ResponseStatus(HttpStatus.OK)
        public ApiNoDataResponse login(
                        @Valid @RequestBody LoginRequest request,
                        HttpServletResponse response) {

                String accessToken = authService.login(request);

                // Tạo cookie với accessToken
                authService.addAccessTokenCookie(response, accessToken);

                return ApiNoDataResponse.builder()
                                .success(true)
                                .statusCode(HttpStatus.OK.value())
                                .message("Login successful")
                                .build();
        }

        @PostMapping("/logout")
        @ResponseStatus(HttpStatus.OK)
        public ApiNoDataResponse logout(HttpServletRequest request, HttpServletResponse response) {
                // Xóa cookie
                authService.clearAccessTokenCookie(request, response);

                return ApiNoDataResponse.builder()
                                .success(true)
                                .statusCode(HttpStatus.OK.value())
                                .message("Logout successful")
                                .build();
        }

        @GetMapping("/account")
        @ResponseStatus(HttpStatus.OK)
        public ApiResponse<AccountResponse> getAccount(Authentication authentication) {
                String userId = authentication.getName(); // Lấy ID từ JWT
                AccountResponse account = authService.getAccount(userId);
                return ApiResponse.<AccountResponse>builder()
                                .success(true)
                                .statusCode(HttpStatus.OK.value())
                                .message("Get account successful!")
                                .data(account)
                                .build();
        }
}