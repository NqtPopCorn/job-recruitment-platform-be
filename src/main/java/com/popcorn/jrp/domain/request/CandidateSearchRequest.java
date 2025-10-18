package com.popcorn.jrp.domain.request;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSearchRequest {
    private String search;
    private String location;
    private String industry;
    private Double experience;
    private String education;
    private CandidateEntity.Gender gender;
}