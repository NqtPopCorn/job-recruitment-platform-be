package com.popcorn.jrp.domain.response.application;

import java.time.LocalDateTime;

import com.popcorn.jrp.domain.response.candidate.CandidateResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantResponseDto {

    private Long id;

    private CandidateResponse candidate;

    private String fileName;
    private String coverLetter;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
