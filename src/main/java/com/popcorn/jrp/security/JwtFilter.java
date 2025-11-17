package com.popcorn.jrp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        System.out.println("[JwtAuthFilter] Request đi qua filter: " + request.getRequestURI());

        String path = request.getRequestURI();

        // Bỏ qua JWT filter cho các endpoint public
        if (path.equals("/api/v1/auth/login") ||
                path.equals("/api/v1/auth/register") ||
                path.equals("/refresh")) {
            chain.doFilter(request, response);
            return;
        }

        // Lấy token từ cookie hoặc header
        String token = extractTokenFromCookie(request);

        if (token == null) {
            token = extractTokenFromHeader(request);
        }

        if (token == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            System.out.println("Access Token: " + token);
            String userId = jwtUtil.extractID(token);
            String role = jwtUtil.extractRole(token);

            if (!jwtUtil.isAccessToken(token)) {
                // sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN,
                // "This is a Refresh Token, rejecting...");
            }

            if (userId != null && jwtUtil.isTokenValid(token, userId)) {
                // Thêm authorities với role
                List<SimpleGrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                logger.info("Token expired");
                // sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                // "Token has expired");
            }
        } catch (Exception e) {
            logger.info("Invalid token or error");
            // sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
            // "Invalid token");
        } finally {
            chain.doFilter(request, response);
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String error = status == HttpServletResponse.SC_UNAUTHORIZED ? "Unauthorized" : "Forbidden";
        String jsonResponse = String.format(
                "{\"statusCode\": %d, \"message\": \"%s\", \"error\": \"%s\"}",
                status, message, error);
        response.getWriter().write(jsonResponse);
    }
}
