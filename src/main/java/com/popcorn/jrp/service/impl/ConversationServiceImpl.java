// File: src/main/java/com/popcorn/jrp/service/impl/ConversationServiceImpl.java

package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.*;
import com.popcorn.jrp.domain.mapper.ConversationMapper;
import com.popcorn.jrp.domain.request.chat.CreateConversationRequestDTO;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.chat.ConversationDetailsDTO;
import com.popcorn.jrp.domain.response.chat.ConversationSummaryDTO;
import com.popcorn.jrp.exception.BadRequestException;
import com.popcorn.jrp.exception.CustomException;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.repository.ConversationMemberRepository;
import com.popcorn.jrp.repository.ConversationRepository;
import com.popcorn.jrp.repository.EmployerRepository;
import com.popcorn.jrp.repository.MessageRepository;
import com.popcorn.jrp.repository.UserRepository;
import com.popcorn.jrp.service.CandidateUploadService;
import com.popcorn.jrp.service.CompanyUploadService;
import com.popcorn.jrp.service.ConversationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMemberRepository conversationMemberRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ConversationMapper conversationMapper;
    // private final CandidateUploadService candidateUploadService;
    // private final CompanyUploadService companyUploadService;
    private final CandidateRepository candidateRepository;
    private final EmployerRepository employerRepository;

    @Transactional
    public ConversationSummaryDTO createPrivateConversation(Long creatorUserId, Long otherUserId) {

        // 1. Tạo ConversationEntity
        ConversationEntity newConversation = new ConversationEntity();
        newConversation.setCreatedAt(java.time.LocalDateTime.now()); // Giả sử BaseEntity không tự set
        ConversationEntity savedConversation = conversationRepository.save(newConversation);

        // 2. Thêm thành viên
        List<UserEntity> users = userRepository.findAllById(List.of(creatorUserId, otherUserId));
        if (users.size() != 2) {
            throw new NotFoundException("A user data");
        }
        List<ConversationMemberEntity> members = new ArrayList<>();

        for (UserEntity user : users) {
            ConversationMemberEntity member = ConversationMemberEntity.builder()
                    .user(user)
                    .conversation(savedConversation)
                    .build();
            members.add(member);
        }
        conversationMemberRepository.saveAll(members);

        savedConversation.setMembers(members);

        // 3. Map sang DTO và trả về (sẽ không có tin nhắn cuối)
        ConversationSummaryDTO dto = convertToSummaryDTO(savedConversation, creatorUserId);
        dto.setOtherUserId(otherUserId);
        return dto;
    }

    @Override
    @Transactional
    public ConversationSummaryDTO findOrCreatePrivateConversation(Long userId1, Long userId2) {
        if (Objects.equals(userId1, userId2)) {
            throw new BadRequestException("2 userIds is equals, please check again");
        }
        // 1. Thử tìm cuộc hội thoại 1-1 đã tồn tại
        Optional<ConversationEntity> existing = conversationRepository.findPrivateConversationByUsers(userId1, userId2);

        if (existing.isPresent()) {
            // Nếu tìm thấy, map và trả về
            return convertToSummaryDTO(existing.get(), userId1);
        }

        // 2. Nếu không tìm thấy, tạo mới
        return createPrivateConversation(userId1, userId2);
    }

    @Override
    public ApiPageResponse<ConversationSummaryDTO> getConversationsForUser(Long userId, Pageable pageable) {
        // 1. Lấy các conversation (đã phân trang) mà user là thành viên
        Page<ConversationEntity> conversationPage = conversationRepository.findConversationsByUserId(userId, pageable);

        // 2. Map Page<Entity> sang Page<DTO>
        // CẢNH BÁO: Đoạn code này gây ra N+1 query.
        // Mỗi lần convertToSummaryDTO, nó sẽ query DB để lấy lastMessage, unreadCount,
        // etc.
        // TODO: Tối ưu bằng cách sử dụng DTO Projection (truy vấn tùy chỉnh trong
        // Repository)
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
                .orElseThrow(() -> new NotFoundException("Conversation with id: " + conversationId));

        // 3. Map
        ConversationDetailsDTO dto = conversationMapper.toDetailsDto(conversation, currentUserId);

        // 4. Set tên hiển thị (logic cho chat 1-1)
        Map<String, String> statsMap = getConversationStatsMap(conversation, currentUserId);
        dto.setDisplayName(statsMap.get("displayName"));
        dto.setDisplayImageUrl(statsMap.get("displayImageUrl"));

        return dto;
    }

    @Override
    @Transactional
    public void leaveConversation(Long conversationId, Long currentUserId) {
        // 1. Tìm thành viên
        ConversationMemberEntity member = conversationMemberRepository
                .findByConversationIdAndUserId(conversationId, currentUserId)
                .orElseThrow(
                        () -> new CustomException(HttpStatus.FORBIDDEN, "You are not a member of this conversation."));

        // 2. Xóa thành viên này (rời khỏi nhóm)
        conversationMemberRepository.delete(member);

        // 3. Nếu đây là thành viên cuối cùng, xóa luôn cuộc hội thoại
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

        // 1. Lấy tên hiển thị, other user id
        Map<String, String> stats = getConversationStatsMap(conversation, currentUserId);
        dto.setDisplayName(stats.get("displayName"));
        dto.setDisplayImageUrl(stats.get("displayImageUrl"));
        if (stats.containsKey("otherUserId")) {
            dto.setOtherUserId(Long.valueOf(stats.get("otherUserId")));
        }

        // 2. Lấy tin nhắn cuối cùng
        Optional<MessageEntity> lastMessageOpt = messageRepository
                .findFirstByConversationIdOrderByCreatedAtDesc(conversation.getId());
        if (lastMessageOpt.isPresent()) {
            MessageEntity lastMessage = lastMessageOpt.get();
            dto.setLastMessageContent(lastMessage.isDeleted() ? "Message deleted" : lastMessage.getContent());
            dto.setLastMessageAt(lastMessage.getCreatedAt());
        }

        // 3. Lấy số tin nhắn chưa đọc
        ConversationMemberEntity currentUserMember = conversationMemberRepository
                .findByConversationIdAndUserId(conversation.getId(), currentUserId)
                .orElse(null); // Nên luôn tồn tại

        if (currentUserMember != null) {
            LocalDateTime lastSeen = currentUserMember.getLastSeenAt();
            long unreadCount = messageRepository.countByConversationIdAndSenderUserIdNotAndCreatedAtAfter(
                    conversation.getId(), currentUserId, lastSeen);
            dto.setUnreadCount(unreadCount);
        }

        return dto;
    }

    private Map<String, String> getConversationStatsMap(ConversationEntity conversation, Long currentUserId) {
        try {
            Map<String, String> result = new HashMap<>();
            // Chat 1-1 ~~ conversation.getMembers().size() == 2
            Optional<ConversationMemberEntity> otherMember = conversation.getMembers().stream()
                    .filter(m -> !m.getUser().getId().equals(currentUserId))
                    .findFirst();

            if (otherMember.isPresent()) {
                UserEntity user = otherMember.get().getUser();
                switch (user.getRole()) {
                    case candidate -> {
                        CandidateEntity candidate = candidateRepository.findByUserId(
                                user.getId())
                                .orElseThrow(() -> new NotFoundException("Candidate with user id " + user.getId()));
                        String displayImageUrl = candidate.getAvatar();
                        String displayName = candidate.getName();
                        result.put("displayImageUrl", displayImageUrl);
                        result.put("displayName", displayName);
                        result.put("otherUserId", user.getId().toString());
                    }
                    case employer -> {
                        EmployerEntity employer = employerRepository.findByUserId(user.getId())
                                .orElseThrow(() -> new NotFoundException("Employer with user id " + user.getId()));
                        String displayImageUrl = employer.getLogo();
                        String displayName = employer.getName();
                        result.put("displayImageUrl", displayImageUrl);
                        result.put("displayName", displayName);
                        result.put("otherUserId", user.getId().toString());
                    }
                    default -> {
                    }
                }
            }
            return result;
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                log.error("Failed to get conversation stats for current user");
                throw new NotFoundException("Please check again, Candidate, or Employer");
            }
            return new HashMap<>();
        }
    }
}