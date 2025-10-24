package com.popcorn.jrp.domain.response.auth;

import lombok.Data;

@Data
public class AccountResponse {
    private String userId;
    private String email;
    private String role;
    private Object data; // Có thể là CandidateData hoặc CompanyData
}