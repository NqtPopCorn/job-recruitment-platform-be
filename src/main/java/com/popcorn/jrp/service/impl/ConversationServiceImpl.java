// File: src/main/java/com/popcorn/jrp/service/impl/ConversationServiceImpl.java

package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.ConversationEntity;
import com.popcorn.jrp.domain.entity.ConversationMemberEntity;
import com.popcorn.jrp.domain.entity.MessageEntity;
import com.popcorn.jrp.domain.entity.UserEntity;
import com.popcorn.jrp.domain.mapper.ConversationMapper;
import com.popcorn.jrp.domain.request.chat.CreateConversationRequestDTO;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.chat.ConversationDetailsDTO;
import com.popcorn.jrp.domain.response.chat.ConversationSummaryDTO;
import com.popcorn.jrp.exception.CustomException;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.ConversationMemberRepository;
import com.popcorn.jrp.repository.ConversationRepository;
import com.popcorn.jrp.repository.MessageRepository;
import com.popcorn.jrp.repository.UserRepository;
import com.popcorn.jrp.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ConversationMapper conversationMapper;

    @Override
    @Transactional
    public ConversationSummaryDTO createConversation(CreateConversationRequestDTO request, Long creatorUserId) {
        // Đảm bảo người tạo luôn có trong danh sách
        if (!request.getUserIds().contains(creatorUserId)) {
            request.getUserIds().add(creatorUserId);
        }

        // 1. Tạo ConversationEntity
        ConversationEntity newConversation = new ConversationEntity();
        newConversation.setCreatedAt(java.time.LocalDateTime.now()); // Giả sử BaseEntity không tự set
        ConversationEntity savedConversation = conversationRepository.save(newConversation);

        // 2. Thêm thành viên
        List<UserEntity> users = userRepository.findAllById(request.getUserIds());
        List<ConversationMemberEntity> members = new ArrayList<>();

        for (UserEntity user : users) {
            ConversationMemberEntity member = ConversationMemberEntity.builder()
                    .user(user)
                    .conversation(savedConversation)
                    .build();
            // @PrePersist trong ConversationMemberEntity sẽ set lastSeenAt
            members.add(member);
        }
        conversationMemberRepository.saveAll(members);

        savedConversation.setMembers(members);

        // 3. Map sang DTO và trả về (sẽ không có tin nhắn cuối)
        return convertToSummaryDTO(savedConversation, creatorUserId);
    }

    @Override
    @Transactional
    public ConversationSummaryDTO findOrCreatePrivateConversation(Long userId1, Long userId2) {
        // 1. Thử tìm cuộc hội thoại 1-1 đã tồn tại
        Optional<ConversationEntity> existing = conversationRepository.findPrivateConversationByUsers(userId1, userId2);

        if (existing.isPresent()) {
            // Nếu tìm thấy, map và trả về
            return convertToSummaryDTO(existing.get(), userId1);
        }

        // 2. Nếu không tìm thấy, tạo mới
        CreateConversationRequestDTO request = new CreateConversationRequestDTO();
        request.setUserIds(List.of(userId1, userId2));

        // Gọi lại hàm createConversation
        return createConversation(request, userId1);
    }

    @Override
    public ApiPageResponse<ConversationSummaryDTO> getConversationsForUser(Long userId, Pageable pageable) {
        // 1. Lấy các conversation (đã phân trang) mà user là thành viên
        Page<ConversationEntity> conversationPage = conversationRepository.findConversationsByUserId(userId, pageable);

        // 2. Map Page<Entity> sang Page<DTO>
        // CẢNH BÁO: Đoạn code này gây ra N+1 query.
        // Mỗi lần convertToSummaryDTO, nó sẽ query DB để lấy lastMessage, unreadCount, etc.
        // TODO: Tối ưu bằng cách sử dụng DTO Projection (truy vấn tùy chỉnh trong Repository)
        return conversationMapper.toApiPageResponse(conversationPage
                .map(conv -> convertToSummaryDTO(conv, userId)));
    }

    @Override
    public ConversationDetailsDTO getConversationDetails(Long conversationId, Long currentUserId) {
        // 1. Kiểm tra quyền
        if (!conversationMemberRepository.existsByConversationIdAndUserId(conversationId, currentUserId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "You are not a member of this conversation.");
        }

        // 2. Lấy thông tin
        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation with id: "+conversationId));

        // 3. Map
        ConversationDetailsDTO dto = conversationMapper.toDetailsDto(conversation);

        // 4. Set tên hiển thị (logic cho chat 1-1)
        dto.setDisplayName(getConversationDisplayName(conversation, currentUserId));
        // dto.setDisplayImageUrl(...);

        return dto;
    }

    @Override
    @Transactional
    public void deleteConversation(Long conversationId, Long currentUserId) {
        // 1. Tìm thành viên
        ConversationMemberEntity member = conversationMemberRepository.findByConversationIdAndUserId(conversationId, currentUserId)
                .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "You are not a member of this conversation."));

        // 2. Xóa thành viên này (rời khỏi nhóm)
        conversationMemberRepository.delete(member);

        // 3. (Tùy chọn) Nếu đây là thành viên cuối cùng, xóa luôn cuộc hội thoại
        long remainingMembers = conversationMemberRepository.countByConversationId(conversationId);
        if (remainingMembers == 0) {
            conversationRepository.deleteById(conversationId);
        }
    }

    // --- CÁC PHƯƠNG THỨC HELPER (GÂY N+1 QUERY) ---

    /**
     * Helper để map đầy đủ ConversationSummaryDTO.
     */
    private ConversationSummaryDTO convertToSummaryDTO(ConversationEntity conversation, Long currentUserId) {
        ConversationSummaryDTO dto = conversationMapper.toSummaryDto(conversation);

        // 1. Lấy tên hiển thị
        dto.setDisplayName(getConversationDisplayName(conversation, currentUserId));
        // dto.setDisplayImageUrl(...);

        // 2. Lấy tin nhắn cuối cùng
        Optional<MessageEntity> lastMessageOpt = messageRepository.findFirstByConversationIdOrderByCreatedAtDesc(conversation.getId());
        if (lastMessageOpt.isPresent()) {
            MessageEntity lastMessage = lastMessageOpt.get();
            dto.setLastMessageContent(lastMessage.isDeleted() ? "Message deleted" : lastMessage.getContent());
            dto.setLastMessageAt(lastMessage.getCreatedAt());
        }

        // 3. Lấy số tin nhắn chưa đọc
        ConversationMemberEntity currentUserMember = conversationMemberRepository.findByConversationIdAndUserId(conversation.getId(), currentUserId)
                .orElse(null); // Nên luôn tồn tại

        if (currentUserMember != null) {
            LocalDateTime lastSeen = currentUserMember.getLastSeenAt();
            long unreadCount = messageRepository.countByConversationIdAndCreatedAtAfter(conversation.getId(), lastSeen);
            dto.setUnreadCount(unreadCount);
        }

        return dto;
    }

    /**
     * Lấy tên hiển thị cho cuộc hội thoại.
     * Nếu là chat 1-1, trả về tên người kia.
     * Nếu là chat nhóm, trả về tên nhóm (nếu có) hoặc "Group chat".
     */
    private String getConversationDisplayName(ConversationEntity conversation, Long currentUserId) {
        if (conversation.getMembers().size() == 2) {
            // Chat 1-1
            Optional<ConversationMemberEntity> otherMember = conversation.getMembers().stream()
                    .filter(m -> !m.getUser().getId().equals(currentUserId))
                    .findFirst();

            if (otherMember.isPresent()) {
                // TODO: Trả về tên thật của user khi UserEntity có
                return "User " + otherMember.get().getUser().getId();
            }
        }

        // TODO: Trả về tên nhóm nếu ConversationEntity có trường 'name'
        return "Group Chat (" + conversation.getMembers().size() + ")";
    }
}