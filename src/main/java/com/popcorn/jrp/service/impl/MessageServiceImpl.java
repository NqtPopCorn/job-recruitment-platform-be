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
import lombok.Locked.Read;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public MessageDTO sendMessage(SendMessageRequestDTO request, Long senderUserId) {
        log.info("sendMessage called with conversationId={} and senderUserId={}",
                request.getConversationId(), senderUserId);

        // 1. Kiểm tra quyền thành viên
        log.debug("Checking if sender is a member of the conversation...");
        boolean isMember = conversationMemberRepository.existsByConversationIdAndUserId(
                request.getConversationId(), senderUserId);
        if (!isMember) {
            log.warn("User {} is not a member of conversation {}", senderUserId, request.getConversationId());
            throw new CustomException(HttpStatus.FORBIDDEN, "User is not a member of this conversation.");
        }
        log.debug("Sender is a member, proceeding...");

        // 2. Lấy entity sender và conversation
        log.debug("Fetching sender user entity...");
        UserEntity sender = userRepository.findById(senderUserId)
                .orElseThrow(() -> {
                    log.error("Sender with id {} not found", senderUserId);
                    return new NotFoundException("Sender with id: " + senderUserId);
                });

        log.debug("Fetching conversation entity...");
        ConversationEntity conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> {
                    log.error("Conversation with id {} not found", request.getConversationId());
                    return new NotFoundException("Conversation with id: " + request.getConversationId());
                });

        log.info("Sender and conversation retrieved successfully");

        // 3. Tạo và lưu message
        log.debug("Creating new message entity with content: {}", request.getContent());
        MessageEntity newMessage = MessageEntity.builder()
                .senderUser(sender)
                .conversation(conversation)
                .content(request.getContent())
                .build();

        log.debug("Saving new message to database...");
        MessageEntity savedMessage = messageRepository.save(newMessage);
        log.info("Message saved with id: {}", savedMessage.getId());

        // 4. Cập nhật updatedAt của conversation (nếu cần)
        log.debug("Updating conversation's updatedAt timestamp...");
        conversationRepository.save(conversation);
        log.info("Conversation updatedAt refreshed for conversationId={}", conversation.getId());

        // 5. Map sang DTO và trả về
        MessageDTO dto = messageMapper.toDto(savedMessage);
        log.info("Returning MessageDTO for messageId={}", dto.getId());

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDTO> getMessagesForConversation(Long conversationId, Long currentUserId,
            Pageable pageable) {
        // 1. Kiểm tra quyền truy cập
        if (!conversationMemberRepository.existsByConversationIdAndUserId(conversationId, currentUserId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "You do not have access to this conversation.");
        }

        // 2. Lấy danh sách tin nhắn đã phân trang từ repository
        Page<MessageEntity> messagePage = messageRepository.findByConversationId(conversationId,
                pageable);
        Page<MessageDTO> messagePageDto = messagePage.map(messageMapper::toDto);
        // 3. Map Page<Entity> sang Page<DTO>
        return messagePageDto;
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
        ConversationMemberEntity member = conversationMemberRepository
                .findByConversationIdAndUserId(conversationId, currentUserId)
                .orElseThrow(() -> new NotFoundException("User is not a member of this conversation."));

        member.setLastSeenAt(LocalDateTime.now());
        conversationMemberRepository.save(member);
        ReadReceiptDTO dto = ReadReceiptDTO.builder()
                .conversationId(conversationId)
                .userId(currentUserId)
                .readAt(member.getLastSeenAt())
                .build();
        return dto;
    }

}