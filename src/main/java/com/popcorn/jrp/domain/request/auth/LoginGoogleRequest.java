package com.popcorn.jrp.domain.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginGoogleRequest {
    @NotBlank(message = "token must not be empty")
    private String token;
}
