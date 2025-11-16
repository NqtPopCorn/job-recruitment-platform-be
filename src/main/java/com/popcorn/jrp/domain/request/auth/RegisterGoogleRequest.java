package com.popcorn.jrp.domain.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterGoogleRequest {
    @NotBlank(message = "token must not be empty")
    private String token;

    @NotBlank(message = "role must not be empty")
    @Pattern(regexp = "^(candidate|employer)$",
            message = "role must be one of: candidate, employer")
    private String role;
}
