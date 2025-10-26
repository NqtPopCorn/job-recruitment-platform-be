
package com.popcorn.jrp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private final long ACCESS_EXPIRATION_TIME = 900000; // 15 phút

    // Tạo Access Token với role
    public String generateAccessToken(String id, String email, String role) {
        try {
            return Jwts.builder()
                    .subject(id)
                    .claim("email", email)
                    .claim("role", role)
                    .claim("type", "access")
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
                    .signWith(SECRET_KEY)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error generating access token: " + e.getMessage());
        }
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
                    .verifyWith(SECRET_KEY)
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