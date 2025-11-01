// File: src/main/java/com/popcorn/jrp/controller/ConversationController.java

package com.popcorn.jrp.controller;

import com.popcorn.jrp.domain.mapper.ConversationMapper;
import com.popcorn.jrp.domain.request.chat.PrivateConversationRequestDTO;
import com.popcorn.jrp.domain.response.ApiDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.chat.ConversationDetailsDTO;
import com.popcorn.jrp.domain.response.chat.ConversationSummaryDTO;
import com.popcorn.jrp.domain.response.chat.MessageDTO;
import com.popcorn.jrp.service.ConversationService;
import com.popcorn.jrp.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final MessageService messageService;
    private final ConversationMapper conversationMapper;

    /**
     * Lấy tất cả các cuộc hội thoại của người dùng hiện tại (đã phân trang).
     * Dùng để hiển thị danh sách "inbox".
     */
    @GetMapping
    public ResponseEntity<ApiPageResponse<ConversationSummaryDTO>> getMyConversations(
            Authentication auth,
            @PageableDefault(size = 20, sort = "updatedAt") Pageable pageable) {
        Long userId = Long.parseLong(auth.getName());

        ApiPageResponse<ConversationSummaryDTO> res = conversationService.getConversationsForUser(
                userId,
                pageable
        );
        res.setStatusCode(200);
        res.setMessage("Success");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<ApiPageResponse<MessageDTO>> getConversationMessages(
            Authentication auth,
            @PathVariable Long conversationId,
            @PageableDefault(size = 20, sort = "createAt") Pageable pageable) {

        Long userId = Long.parseLong(auth.getName());
        ApiPageResponse<MessageDTO> res = messageService.getMessagesForConversation(conversationId, userId, pageable);
        res.setStatusCode(200);
        res.setMessage("Success");
        return ResponseEntity.ok(res);
    }

    /**
     * Tìm hoặc tạo một cuộc hội thoại riêng tư (1-on-1) với một người dùng khác.
     * API này đảm bảo chỉ có một cuộc hội thoại 1-1 duy nhất giữa hai người.
     */
    @PostMapping("/private")
    public ResponseEntity<ApiDataResponse<ConversationSummaryDTO>> findOrCreatePrivateConversation(
            @Valid @RequestBody PrivateConversationRequestDTO request,
            Authentication auth) {

        Long userId = Long.parseLong(auth.getName());
        ConversationSummaryDTO conversation = conversationService.findOrCreatePrivateConversation(
                userId,
                request.getOtherUserId()
        );
        return ResponseEntity.ok(ApiDataResponse.<ConversationSummaryDTO>builder()
                .statusCode(200)
                .message("Private conversation created")
                .data(conversation)
                .build());
    }

    /**
     * Lấy thông tin chi tiết của một cuộc hội thoại (gồm danh sách thành viên).
     */
    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiDataResponse<ConversationDetailsDTO>> getConversationDetails(
            @PathVariable Long conversationId,
            Authentication auth) {
        Long userId = Long.parseLong(auth.getName());

        ConversationDetailsDTO details = conversationService.getConversationDetails(
                conversationId,
                userId
        );
        return ResponseEntity.ok(ApiDataResponse.<ConversationDetailsDTO>builder()
                        .statusCode(200)
                        .message("Conversation details")
                        .data(details)
                .build());
    }

    /**
     * Xóa/rời khỏi một cuộc hội thoại.
     * @return 204 No Content.
     */
    @DeleteMapping("/{conversationId}/leave")
    public ResponseEntity<ApiDataResponse<Void>> deleteConversation(
            @PathVariable Long conversationId,
            Authentication auth) {
        Long userId = Long.parseLong(auth.getName());

        conversationService.leaveConversation(conversationId, userId);
        return ResponseEntity.ok(ApiDataResponse.<Void>builder()
                    .statusCode(204)
                    .message("Conversation deleted")
                .build());
    }

}