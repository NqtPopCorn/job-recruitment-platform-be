package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.chat.CreateConversationRequestDTO;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.chat.ConversationDetailsDTO;
import com.popcorn.jrp.domain.response.chat.ConversationSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing conversations.
 */
public interface ConversationService {

    /**
     * Creates a new conversation with the given members.
     * This method will also create the corresponding ConversationMemberEntity entries.
     *
     * @param request DTO containing the list of user IDs to include.
     * @param creatorUserId The ID of the user creating the conversation.
     * @return A summary DTO of the newly created conversation.
     */
    ConversationSummaryDTO createConversation(CreateConversationRequestDTO request, Long creatorUserId);

    /**
     * Finds or creates a private (1-on-1) conversation between two users.
     * If a conversation already exists, it returns the existing one.
     *
     * @param userId1 ID of the first user.
     * @param userId2 ID of the second user.
     * @return A summary DTO of the private conversation.
     */
    ConversationSummaryDTO findOrCreatePrivateConversation(Long userId1, Long userId2);

    /**
     * Retrieves a paginated list of conversation summaries for a specific user.
     * This is used to display the user's "inbox" or list of chats.
     *
     * @param userId The ID of the user.
     * @param pageable Pagination information.
     * @return A Page of ConversationSummaryDTOs.
     */
    ApiPageResponse<ConversationSummaryDTO> getConversationsForUser(Long userId, Pageable pageable);

    /**
     * Retrieves the detailed information for a single conversation,
     * including the list of members.
     *
     * @param conversationId The ID of the conversation to fetch.
     * @param currentUserId The ID of the user requesting the details (for permission checking).
     * @return A ConversationDetailsDTO.
     */
    ConversationDetailsDTO getConversationDetails(Long conversationId, Long currentUserId);

    /**
     * Deletes a conversation for a specific user.
     * (This might be a soft delete or removing the user from the conversation members).
     *
     * @param conversationId The ID of the conversation.
     * @param currentUserId The ID of the user performing the action.
     */
    void deleteConversation(Long conversationId, Long currentUserId);
}