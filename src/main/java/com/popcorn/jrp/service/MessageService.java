package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.chat.SendMessageRequestDTO;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.chat.MessageDTO;
import com.popcorn.jrp.domain.response.chat.ReadReceiptDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing messages within conversations.
 */
public interface MessageService {

    /**
     * Sends a new message in a conversation.
     * This will create a MessageEntity and potentially update
     * the conversation's last activity timestamp.
     *
     * @param request DTO containing conversation ID and message content.
     * @param senderUserId The ID of the user sending the message.
     * @return A DTO representation of the message.
     */
    MessageDTO sendMessage(SendMessageRequestDTO request, Long senderUserId);

    /**
     * Retrieves a paginated list of messages for a specific conversation.
     * Messages are typically ordered from newest to oldest.
     *
     * @param conversationId The ID of the conversation.
     * @param currentUserId The ID of the user requesting messages (for permission checking).
     * @param pageable Pagination information.
     * @return A Page of MessageDTOs.
     */
    ApiPageResponse<MessageDTO> getMessagesForConversation(Long conversationId, Long currentUserId, Pageable pageable);

    /**
     * Deletes a message. This should be a soft delete (setting isDeleted = true)
     * as per your MessageEntity.
     *
     * @param messageId The ID of the message to delete.
     * @param currentUserId The ID of the user attempting to delete (must be the sender).
     */
    void deleteMessage(Long messageId, Long currentUserId);

    /**
     * Marks all messages in a conversation as "read" for a specific user
     * by updating the 'lastSeenAt' timestamp in ConversationMemberEntity.
     *
     * @param conversationId The ID of the conversation.
     * @param currentUserId The ID of the user who has read the messages.
     */
    ReadReceiptDTO markConversationAsRead(Long conversationId, Long currentUserId);
}
