package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.UserEntity;
import com.popcorn.jrp.domain.request.auth.LoginRequest;
import com.popcorn.jrp.domain.response.auth.AccountResponse;
import com.popcorn.jrp.exception.CustomException;
import com.popcorn.jrp.repository.UserRepository;
import com.popcorn.jrp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements com.popcorn.jrp.service.AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
//    private final CandidateService candidateService;
//    private final CompanyService companyService;

    public String login(LoginRequest request) {
        try {
            // Tìm user theo email
            UserEntity user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new CustomException(
                            HttpStatus.UNAUTHORIZED,
                            "Invalid credentials"
                    ));

            // Kiểm tra password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }

            // Tạo access token với ID
            return jwtUtil.generateAccessToken(String.valueOf(user.getId()));

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "AuthService.login: Unexpected error occurred",
                    e.getMessage()
            );
        }
    }
//
//    public AccountResponse getAccount(String userId) {
//        try {
//            // Tìm user theo ID
//            UserEntity user = userRepository.findById(Long.parseLong(userId))
//                    .orElseThrow(() -> new CustomException(
//                            HttpStatus.UNAUTHORIZED,
//                            "Invalid token"
//                    ));
//
//            AccountResponse response = new AccountResponse();
//            response.setUserId(String.valueOf(user.getId()));
//            response.setEmail(user.getEmail());
//            response.setRole(user.getRole().name());
//
//            // Lấy data tùy theo role
//            switch (user.getRole()) {
//                case candidate:
//                    Object candidateData = candidateService.getCandidateByUserId(user.getId());
//                    response.setData(candidateData);
//                    break;
//
//                case employer:
//                    Object companyData = companyService.getCompanyByUserId(user.getId());
//                    response.setData(companyData);
//                    break;
//
//                case admin:
//                    // Admin không có data bổ sung
//                    response.setData(null);
//                    break;
//
//                default:
//                    throw new CustomException(
//                            HttpStatus.BAD_REQUEST,
//                            "Unsupported user role: " + user.getRole().name()
//                    );
//            }
//
//            return response;
//
//        } catch (CustomException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new CustomException(
//                    HttpStatus.INTERNAL_SERVER_ERROR,
//                    "AuthService.getAccount: Unexpected error occurred",
//                    e.getMessage()
//            );
//        }
//    }
}