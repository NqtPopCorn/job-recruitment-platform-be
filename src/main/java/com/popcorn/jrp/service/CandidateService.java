package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.candidate.CandidateSearchRequest;
import com.popcorn.jrp.domain.response.candidate.CandidateStatisticsResponse;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateDto;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateDetailsResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateResponse;
import com.popcorn.jrp.domain.response.candidate.JobAppliedRecentlyResponse;
import com.popcorn.jrp.domain.response.candidate.SoftDeleteCandidateResponse;
import com.popcorn.jrp.domain.response.notification.NotificationResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CandidateService {
    ApiPageResponse<CandidateResponse> getCandidates(CandidateSearchRequest request, Pageable pageable);

    CandidateDetailsResponse getCandidateById(Long id);

    CandidateDetailsResponse getCandidateByUserId(Long userId);

    CandidateDetailsResponse updateCandidate(Long id, UpdateCandidateDto dto);

    SoftDeleteCandidateResponse softDeleteCandidate(Long id);

    void deleteCandidate(Long id);

    List<String> getIndustryList();

    List<String> getSkillList();

    CandidateStatisticsResponse getCandidateStatistics(Long candidateId);

    List<NotificationResponse> getLatestNotifications(Long candidateId);

    List<JobAppliedRecentlyResponse> getRecentlyAppliedJobs(Long candidateId, Integer limit);
}
