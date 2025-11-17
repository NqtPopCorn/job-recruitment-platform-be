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

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null)
            return message;

        if (accessor.getCommand() == StompCommand.CONNECT) {
            log.info("[JwtChannelInterceptor] CONNECT...");
        }

        // Xác thực cho SEND / SUBSCRIBE
        if (accessor.getCommand() == StompCommand.SEND ||
                accessor.getCommand() == StompCommand.SUBSCRIBE) {

            // LẤY USER TỪ SESSION ATTRIBUTES
            UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) accessor
                    .getSessionAttributes().get("user");

            if (accessor.getSessionAttributes() == null || accessor.getSessionAttributes().get("user") == null) {
                log.error("Không tìm thấy user trong SessionAttributes!");
                throw new CustomException(HttpStatus.UNAUTHORIZED, "WebSocket unauthorized");
            }

            // Gắn lại vào accessor → để @MessageMapping nhận được Principal
            accessor.setUser(user);

            log.info("[JwtChannelInterceptor] User hợp lệ: {}", user.getName());
        }

        return message;
    }
}
