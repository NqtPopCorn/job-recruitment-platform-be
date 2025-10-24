package com.popcorn.jrp.domain.request.candidate;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSearchRequest {
    private String search;
    private String location;
    private String industry;
    private Integer experience;
    private String education;
    private String gender;
}