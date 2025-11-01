package com.popcorn.jrp.security;

import com.popcorn.jrp.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (Objects.requireNonNull(accessor).getCommand() == StompCommand.CONNECT) {
            log.info("🔥 [JwtChannelInterceptor] Client is connecting...");

            String authHeader = accessor.getFirstNativeHeader("Authorization");
            log.info("Authorization header: {}", authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                try {
                    String userId = jwtUtil.extractID(token);

                    if (userId != null && jwtUtil.isTokenValid(token, userId)) {
                        String role = jwtUtil.extractRole(token);
                        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userId, null, authorities);

                        accessor.setUser(authenticationToken);
                        log.info("✅ [JwtChannelInterceptor] Authenticated user: {}, role: {}", userId, role);
                    }
                } catch (Exception e) {
                    log.error("❌ [JwtChannelInterceptor] Authentication failed: {}", e.getMessage());
                    throw new CustomException(HttpStatus.UNAUTHORIZED, "Authentication failed");
                }
            }
        }
        return message;
    }
}
