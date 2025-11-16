package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.entity.UserEntity;
import com.popcorn.jrp.domain.request.auth.LoginGoogleRequest;
import com.popcorn.jrp.domain.request.auth.LoginRequest;
import com.popcorn.jrp.domain.request.auth.RegisterGoogleRequest;
import com.popcorn.jrp.domain.request.auth.RegisterRequest;
import com.popcorn.jrp.domain.response.auth.AccountResponse;
import com.popcorn.jrp.exception.BadRequestException;
import com.popcorn.jrp.exception.CustomException;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.repository.EmployerRepository;
import com.popcorn.jrp.repository.UserRepository;
import com.popcorn.jrp.security.GoogleTokenVerifier;
import com.popcorn.jrp.security.JwtUtil;

import com.popcorn.jrp.service.GoogleAuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;
import java.math.BigDecimal;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import com.popcorn.jrp.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, GoogleAuthService {

    @Value("${jwt.token.expiration:1h}") // đọc từ application.properties, mặc định 1h
    private String jwtTokenExpiration;

    @Value("${role.candidate}")
    private String roleCandidate;

    @Value("${role.employer}")
    private String roleEmployer;

    @Value("${role.admin}")
    private String roleAdmin;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CandidateRepository candidateRepository;
    private final EmployerRepository employerRepository;
    private final GoogleTokenVerifier googleTokenVerifier;

    @Transactional
    public UserEntity register(RegisterRequest request) {
        try {
            // Kiểm tra email đã tồn tại
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new CustomException(
                        HttpStatus.BAD_REQUEST,
                        "Email already exists");
            }

            // Tạo user mới
            UserEntity user = UserEntity.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(parseRole(request.getRole()))
                    .build();

            UserEntity newUser = userRepository.save(user);

            switch (user.getRole()) {
                case candidate:
                    CandidateEntity candidate = CandidateEntity.builder()
                            .user(newUser)
                            .name("") // trống
                            .birthday(null)
                            .avatar("")
                            .industry("")
                            .designation("")
                            .location("")
                            .description("")
                            .experience(null)
                            .currentSalary(BigDecimal.valueOf(0))
                            .expectedSalary(BigDecimal.valueOf(0))
                            .gender("")
                            .email(newUser.getEmail())
                            .phone("")
                            .hourlyRate(null)
                            .city("")
                            .country("")
                            .languages(new ArrayList<>())
                            .skills(new ArrayList<>())
                            .educationLevel("")
                            .status(true)
                            .build();

                    candidateRepository.save(candidate);
                    break;

                case employer:
                    EmployerEntity employer = EmployerEntity.builder()
                            .user(newUser)
                            .address("")
                            .city("")
                            .country("")
                            .description("")
                            .email(request.getEmail())
                            .foundedIn(1900)
                            .logo("")
                            .name("")
                            .phone("")
                            .primaryIndustry("")
                            .size("")
                            .socialMedias("[]")
                            .build();
                    employerRepository.save(employer);
                    break;

                case admin:
                    // Admin không có data bổ sung
                    break;

                default:
                    throw new CustomException(
                            HttpStatus.BAD_REQUEST,
                            "Unsupported user role: " + user.getRole().name());
            }

            return newUser;
        } catch (Exception e) {
            throw new CustomException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "AuthService.register: Unexpected error occurred",
                    e.getMessage());
        }
    }

    @Override
    public String login(LoginRequest request) {
        try {
            // Tìm user theo email
            UserEntity user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new CustomException(
                            HttpStatus.UNAUTHORIZED,
                            "User not found"));

            // Kiểm tra password
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid password");
            }

            // Tạo access token với ID, email và role
            return jwtUtil.generateAccessToken(
                    String.valueOf(user.getId()),
                    user.getEmail(),
                    user.getRole().name());

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "AuthService.login: Unexpected error occurred",
                    e.getMessage());
        }
    }

    @Override
    public AccountResponse getAccount(String userId) {
        try {
            UserEntity user = userRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new CustomException(
                            HttpStatus.UNAUTHORIZED,
                            "Invalid token"));

            AccountResponse response = new AccountResponse();
            response.setUserId(user.getId());
            response.setEmailLogin(user.getEmail());
            response.setRole(user.getRole().name());

            // Lấy data tùy theo role
            switch (user.getRole()) {
                case candidate:
                    CandidateEntity candidate = candidateRepository.findByUserId(Long.valueOf(userId))
                            .orElseThrow(() -> new NotFoundException("Candidate"));
                    response.setId(candidate.getId());
                    response.setName(candidate.getName());
                    response.setImageUrl(candidate.getAvatar());
                    break;

                case employer:
                    EmployerEntity employer = employerRepository.findByUserId(Long.valueOf(userId))
                            .orElseThrow(() -> new NotFoundException("Candidate"));
                    response.setId(employer.getId());
                    response.setName(employer.getName());
                    response.setImageUrl(employer.getLogo());
                    break;

                case admin:
                    // Admin không có data bổ sung
                    break;

                default:
                    throw new CustomException(
                            HttpStatus.BAD_REQUEST,
                            "Unsupported user role: " + user.getRole().name());
            }

            return response;

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "AuthService.getAccount: Unexpected error occurred",
                    e.getMessage());
        }
    }

    @Override
    public void addAccessTokenCookie(HttpServletResponse response, String accessToken) {
        long maxAgeMs = parseExpirationToMs(jwtTokenExpiration); // dùng hàm bạn đã viết ở trên
        int maxAgeSeconds = (int) (maxAgeMs / 1000); // Cookie nhận đơn vị giây

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // ⚠️ Chỉ nên để true khi chạy HTTPS (production)
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds); // tính động từ config

        // SameSite=Lax không hỗ trợ trực tiếp trong Cookie API, cần set header thủ công
        response.addHeader("Set-Cookie",
                String.format("accessToken=%s; Max-Age=%d; Path=/; HttpOnly; SameSite=Lax%s",
                        accessToken,
                        maxAgeSeconds,
                        cookie.getSecure() ? "; Secure" : ""));
    }

    @Override
    public void clearAccessTokenCookie(HttpServletRequest request, HttpServletResponse response) {
        // Kiểm tra cookie có tồn tại không
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không tìm thấy cookie để xóa");
        }

        boolean found = false;
        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) {
                // Xóa cookie bằng cách set MaxAge = 0
                Cookie deleteCookie = new Cookie("accessToken", null);
                deleteCookie.setHttpOnly(true);
                deleteCookie.setSecure(false); // đổi thành true nếu chạy HTTPS
                deleteCookie.setPath("/");
                deleteCookie.setMaxAge(0); // <--- xoá cookie

                // SameSite=Lax không có sẵn trong Cookie API → thêm thủ công
                response.addHeader("Set-Cookie",
                        "accessToken=; Max-Age=0; Path=/; HttpOnly; SameSite=Lax" +
                                (deleteCookie.getSecure() ? "; Secure" : ""));

                response.addCookie(deleteCookie);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không tìm thấy cookie để xóa");
        }
    }

    private UserEntity.Role parseRole(String roleStr) {
        return Arrays
                .stream(UserEntity.Role.values())
                .filter(r -> r.name().equalsIgnoreCase(roleStr))
                .findFirst()
                .orElseThrow(() -> new CustomException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid role: " + roleStr));
    }

    public long parseExpirationToMs(String exp) {
        // Regex kiểm tra định dạng như "1d", "30m", "10h", ...
        var pattern = Pattern.compile("^(\\d+)([smhd])$");
        var matcher = pattern.matcher(exp);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid expiration format: " + exp);
        }

        long value = Long.parseLong(matcher.group(1));
        String unit = matcher.group(2);

        return switch (unit) {
            case "s" -> value * 1000; // giây
            case "m" -> value * 60 * 1000; // phút
            case "h" -> value * 60 * 60 * 1000; // giờ
            case "d" -> value * 24 * 60 * 60 * 1000; // ngày
            default -> throw new IllegalArgumentException("Unknown time unit: " + unit);
        };
    }

    @Override
    public String loginWithGoogle(LoginGoogleRequest request) {
        var payload = googleTokenVerifier.verify(request.getToken());
        if(!payload.getEmailVerified()) {
            throw new BadRequestException("Email is not verified by Google");
        }
        String email = payload.getEmail();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email " + email));
        return jwtUtil.generateAccessToken(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getRole().name());
    }

    @Override
    @Transactional
    public String registerWithGoogle(RegisterGoogleRequest request) {
        var payload = googleTokenVerifier.verify(request.getToken());
        if(!payload.getEmailVerified()) {
            throw new BadRequestException("Email is not verified by Google");
        }
        String email = payload.getEmail();
        String name = payload.get("name").toString();
        // Kiểm tra email đã tồn tại
        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists");
        }

        // Tạo user mới
        UserEntity user = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(UUID.randomUUID().toString())) // random password
                .role(parseRole(request.getRole()))
                .build();
        userRepository.save(user);

        switch (user.getRole()) {
            case candidate -> {
                CandidateEntity candidate = CandidateEntity.builder()
                        .name(name)
                        .email(email)
                        .build();
                candidate.setUser(user);
                candidateRepository.save(candidate);
            }
            case employer -> {
                EmployerEntity employer = EmployerEntity.builder()
                        .name(name)
                        .email(email)
                        .build();
                employer.setUser(user);
                employerRepository.save(employer);
            }
            default -> throw new BadRequestException("Invalid role: " + request.getRole());
        }
        return jwtUtil.generateAccessToken(
                String.valueOf(user.getId()),
                user.getEmail(),
                user.getRole().name());
    }
}