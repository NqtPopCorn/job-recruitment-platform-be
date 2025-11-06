package com.popcorn.jrp.domain.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "email should not be empty")
    @Email(message = "email must be an email")
    private String email;

    @NotBlank(message = "password should not be empty")
    private String password;

    @NotBlank(message = "role should not be empty")
    @Pattern(regexp = "^(admin|candidate|employer)$",
            message = "role must be one of: admin, candidate, employer")
    private String role;
}