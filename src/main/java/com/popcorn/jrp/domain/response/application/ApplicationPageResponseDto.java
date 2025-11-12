package com.popcorn.jrp.domain.response.application;

import java.time.LocalDateTime;

import com.popcorn.jrp.domain.response.job.JobResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationPageResponseDto {
    private Long id;
    private JobResponseDto job;
    private String filename;
    private String coverLetter;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
