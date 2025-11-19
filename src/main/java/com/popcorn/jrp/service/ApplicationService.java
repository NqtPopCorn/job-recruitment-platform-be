package com.popcorn.jrp.service;

import java.util.List;

import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.candidate.JobAppliedRecentlyResponse;
import com.popcorn.jrp.domain.response.employer.RecentApplicantResponse;
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

    List<JobAppliedRecentlyResponse> getRecentAppliedJobs(Long candidateId, Integer limit);

    ApiPageResponse<JobAppliedRecentlyResponse> getAllAppliedJobs(Long candidateId, Pageable pageable);

    boolean hasAppliedJob(Long candidateId, Long jobId);

    ApiPageResponse<RecentApplicantResponse> getAllApplicantsByEmployer(Long employerId, Pageable pageable);
}
