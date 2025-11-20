
package com.popcorn.jrp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKeyString;

    @Value("${jwt.token.expiration}")
    private String tokenExpiration; // ví dụ: "1d" hoặc "15m"

    @Value("${jwt.audience}")
    private String audience;

    @Value("${jwt.issuer}")
    private String issuer;

    private SecretKey secretKey;
    private long accessExpirationMs;

    @PostConstruct
    public void init() {
        this.secretKey = getSigningKey();
        this.accessExpirationMs = parseExpirationTime(tokenExpiration);
    }

    // Hàm parse "1d", "15m", "900000" -> mili giây
    private long parseExpirationTime(String value) {
        value = value.trim().toLowerCase();
        if (value.endsWith("d")) {
            return TimeUnit.DAYS.toMillis(Long.parseLong(value.replace("d", "")));
        } else if (value.endsWith("h")) {
            return TimeUnit.HOURS.toMillis(Long.parseLong(value.replace("h", "")));
        } else if (value.endsWith("m")) {
            return TimeUnit.MINUTES.toMillis(Long.parseLong(value.replace("m", "")));
        } else {
            return Long.parseLong(value); // default: milliseconds
        }
    }

    // Tạo Access Token với role
    public String generateAccessToken(String id, String email, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessExpirationMs);

        return Jwts.builder()
                .subject(id)
                .claim("email", email)
                .claim("role", role)
                .claim("type", "access")
                .issuedAt(now)
                .expiration(expiry)
                .issuer(issuer)
                .audience().add(audience).and()
                .signWith(secretKey)
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
    }

    // Lấy ID từ token
    public String extractID(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Error extracting ID from token: " + e.getMessage());
        }
    }

    // Lấy email từ token
    public String extractEmail(String token) {
        try {
            return getClaims(token).get("email", String.class);
        } catch (Exception e) {
            throw new RuntimeException("Error extracting email from token: " + e.getMessage());
        }
    }

    // Lấy role từ token
    public String extractRole(String token) {
        try {
            return getClaims(token).get("role", String.class);
        } catch (Exception e) {
            throw new RuntimeException("Error extracting role from token: " + e.getMessage());
        }
    }

    // Kiểm tra token có hợp lệ không
    public boolean isTokenValid(String token, String ID) {
        try {
            return extractID(token).equals(ID) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAccessToken(String token) {
        try {
            Claims claims = getClaims(token);
            return "access".equals(claims.get("type"));
        } catch (Exception e) {
            return false;
        }
    }

    // Lấy thông tin từ token
    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing token: " + e.getMessage());
        }
    }

    // Kiểm tra token có hết hạn không
    private boolean isTokenExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}