package com.popcorn.jrp.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiEventChatResponse<T> {
    private int statusCode;
    private String message;
    private String eventType; // e.g., "NEW_MESSAGE", "DELETE_MESSAGE", "READ_RECEIPT"
    private T data;
}
