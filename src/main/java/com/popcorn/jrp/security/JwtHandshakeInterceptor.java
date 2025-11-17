package com.popcorn.jrp.security;

import java.util.List;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        log.info("HandshakeInterceptor: Đang kiểm tra Cookie...");

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            // LẤY COOKIE
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {

                    if ("accessToken".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        log.info("Tìm thấy accessToken trong Cookie");

                        try {
                            String userId = jwtUtil.extractID(token);

                            if (jwtUtil.isTokenValid(token, userId)) {
                                String role = jwtUtil.extractRole(token);

                                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                        userId,
                                        null,
                                        List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));

                                // LƯU USER VÀO WEBSOCKET SESSION
                                attributes.put("user", auth);
                                log.info("Xác thực thành công userId={}, role={}", userId, role);
                                return true;
                            }

                        } catch (Exception e) {
                            log.error("Token không hợp lệ: {}", e.getMessage());
                            return false;
                        }
                    }
                }
            }
        }

        log.warn("Không tìm thấy accessToken trong Cookie!");
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        // Không cần làm gì ở đây
    }
}
