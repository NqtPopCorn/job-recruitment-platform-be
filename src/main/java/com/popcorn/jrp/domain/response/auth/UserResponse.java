package com.popcorn.jrp.domain.response.auth;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    private String role;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
