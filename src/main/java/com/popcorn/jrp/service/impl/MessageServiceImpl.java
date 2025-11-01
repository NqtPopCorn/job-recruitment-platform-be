// File: src/main/java/com/popcorn/jrp/service/impl/MessageServiceImpl.java

package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.ConversationEntity;
import com.popcorn.jrp.domain.entity.ConversationMemberEntity;
import com.popcorn.jrp.domain.entity.MessageEntity;
import com.popcorn.jrp.domain.entity.UserEntity;
import com.popcorn.jrp.domain.mapper.MessageMapper;
import com.popcorn.jrp.domain.request.chat.SendMessageRequestDTO;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.chat.MessageDTO;
import com.popcorn.jrp.domain.response.chat.ReadReceiptDTO;
import com.popcorn.jrp.exception.CustomException;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.ConversationMemberRepository;
import com.popcorn.jrp.repository.ConversationRepository;
import com.popcorn.jrp.repository.MessageRepository;
import com.popcorn.jrp.repository.UserRepository;
import com.popcorn.jrp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public MessageDTO sendMessage(SendMessageRequestDTO request, Long senderUserId) {
        // 1. Kiểm tra xem người gửi có phải là thành viên của cuộc hội thoại không
        if (!conversationMemberRepository.existsByConversationIdAndUserId(request.getConversationId(), senderUserId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "User is not a member of this conversation.");
        }

        // 2. Lấy các entity cần thiết từ database
        UserEntity sender = userRepository.findById(senderUserId)
                .orElseThrow(() -> new NotFoundException("Sender with id: " + senderUserId));

        ConversationEntity conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new NotFoundException("Conversation with id: " + request.getConversationId()));

        // 3. Tạo và lưu MessageEntity mới
        MessageEntity newMessage = MessageEntity.builder()
                .senderUser(sender)
                .conversation(conversation)
                .content(request.getContent())
                .isDeleted(false)
                .build();

        // onCreate() trong BaseEntity sẽ tự động set createdAt và updatedAt

        MessageEntity savedMessage = messageRepository.save(newMessage);

        // (Tùy chọn) Cập nhật lại updatedAt của conversation để nó nổi lên đầu danh sách
        conversation.setUpdatedAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        // 4. Map sang DTO để trả về
        return messageMapper.toDto(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiPageResponse<MessageDTO> getMessagesForConversation(Long conversationId, Long currentUserId, Pageable pageable) {
        // 1. Kiểm tra quyền truy cập
        if (!conversationMemberRepository.existsByConversationIdAndUserId(conversationId, currentUserId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "You do not have access to this conversation.");
        }

        // 2. Lấy danh sách tin nhắn đã phân trang từ repository
        Page<MessageEntity> messagePage = messageRepository.findByConversationIdOrderByCreatedAtDesc(conversationId, pageable);

        // 3. Map Page<Entity> sang Page<DTO>
        return messageMapper.toApiPageResponse(messagePage.map(messageMapper::toDto));
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId, Long currentUserId) {
        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message not found with id: " + messageId));

        // Chỉ người gửi mới có quyền xóa tin nhắn của mình
        if (!message.getSenderUser().getId().equals(currentUserId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "You can only delete your own messages.");
        }

        // soft delete
        message.setDeleted(true);
        messageRepository.save(message);
    }

    @Override
    @Transactional
    public ReadReceiptDTO markConversationAsRead(Long conversationId, Long currentUserId) {
        ConversationMemberEntity member = conversationMemberRepository.findByConversationIdAndUserId(conversationId, currentUserId)
                .orElseThrow(() -> new NotFoundException("User is not a member of this conversation."));

        member.setLastSeenAt(LocalDateTime.now());
        conversationMemberRepository.save(member);
        return new ReadReceiptDTO(conversationId, currentUserId, member.getLastSeenAt());
    }


}