package com.popcorn.jrp.domain.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Long userId;
    private String emailLogin;
    private String role;
    private Long id;
    private String name;
    private String imageUrl;
}