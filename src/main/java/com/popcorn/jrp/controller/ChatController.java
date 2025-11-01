package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.request.chat.DeleteMessageDto;
import com.popcorn.jrp.domain.request.chat.MarkAsReadRequestDTO;
import com.popcorn.jrp.domain.request.chat.SendMessageRequestDTO;
import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.chat.MessageDTO;
import com.popcorn.jrp.domain.response.chat.ReadReceiptDTO;
import com.popcorn.jrp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.Instant;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Xử lý khi người dùng gửi một tin nhắn mới.
     */
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(
            @Payload SendMessageRequestDTO request,
            @AuthenticationPrincipal Principal principal) {
        Long userId = Long.parseLong(principal.getName());

        MessageDTO message = messageService.sendMessage(request, userId);

        String destination = "/topic/conversation/" + message.getConversationId();

        messagingTemplate.convertAndSend(destination, ApiDataResponse.builder()
                        .statusCode(200)
                        .message("New message sent to " + destination)
                        .data(message)
                .build());
    }

    /**
     * Delete message
     */
    @MessageMapping("/chat.deleteMessage")
    public void deleteMessage(
            @Payload DeleteMessageDto request,
            @AuthenticationPrincipal Principal principal) {
        Long userId = Long.parseLong(principal.getName());

        messageService.deleteMessage(request.getMessageId(), userId);

        String destination = "/topic/conversation/" + request.getConversationId();

        messagingTemplate.convertAndSend(destination, ApiDataResponse.builder()
                .statusCode(200)
                .message("Message deleted")
                .data(Map.of("messageId", request.getMessageId()))
                .build());
    }


    /**
     * Xử lý khi người dùng "đã xem" tin nhắn trong một cuộc hội thoại.
     * Client sẽ gửi tin nhắn đến destination: /app/chat.markAsRead
     */
    @MessageMapping("/chat.markAsRead")
    public void markAsRead(
            @Payload MarkAsReadRequestDTO request,
            @AuthenticationPrincipal Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        ReadReceiptDTO res = messageService.markConversationAsRead(request.getConversationId(), userId);

         String destination = "/topic/conversation/" + request.getConversationId();
         messagingTemplate.convertAndSend(destination, ApiDataResponse.builder()
                 .statusCode(200)
                 .message("Marked as read")
                 .data(res)
                 .build());
    }

    @GetMapping("chat-ui")
    public String chatTest(Model model) {
        return "chatUI";
    }

    /*
    @MessageMapping("/chat.typing")
    public void handleTyping(
            @Payload TypingRequestDTO request,
            @AuthenticationPrincipal UserEntity currentUser) {
        
        String destination = "/topic/conversation/" + request.getConversationId() + "/typing";
        messagingTemplate.convertAndSend(destination, new TypingEventDTO(currentUser.getId(), true));
    }
    */
}