package com.popcorn.jrp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.popcorn.jrp.domain.query.ApplicationQueryDto;
import com.popcorn.jrp.domain.request.application.CreateApplicationRequestDto;
import com.popcorn.jrp.domain.response.application.ApplicantResponseDto;
import com.popcorn.jrp.domain.response.application.ApplicationPageResponseDto;
import com.popcorn.jrp.domain.response.application.ApplicationResponseDto;

public interface ApplicationService {

    public ApplicationResponseDto createApplication(CreateApplicationRequestDto dto);

    public boolean hasCandidateApplied(Long candidateId, Long jobId);

    public Page<ApplicationPageResponseDto> getApplicationsByCandidateId(Long candidateId, Pageable pageable,
            ApplicationQueryDto queryDto);

    public List<ApplicantResponseDto> getApplicantsAppliedByJobId(Long jobId);

    public void updateApplicantStatus(Long applicationId, String status);
}
