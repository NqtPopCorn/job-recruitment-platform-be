package com.popcorn.jrp.domain.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "email should not be empty")
    @Email(message = "email must be an email")
    private String email;

    @NotBlank(message = "password should not be empty")
    private String password;
}