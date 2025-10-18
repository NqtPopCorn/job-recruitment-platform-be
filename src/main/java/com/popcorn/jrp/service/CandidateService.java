package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.CandidateSearchRequest;
import com.popcorn.jrp.domain.request.CreateCandidateDto;
import com.popcorn.jrp.domain.request.UpdateCandidateDto;
import com.popcorn.jrp.domain.response.CandidateDetailsResponse;
import com.popcorn.jrp.domain.response.CandidateResponse;
import com.popcorn.jrp.domain.response.SoftDeleteCandidateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CandidateService {
    Page<CandidateResponse> getCandidates(CandidateSearchRequest request, Pageable pageable);
    CandidateDetailsResponse getCandidateById(String id);
    CandidateDetailsResponse getCandidateByUserId(String userId);
    CandidateDetailsResponse createCandidate(CreateCandidateDto dto);
    CandidateDetailsResponse updateCandidate(String id, UpdateCandidateDto dto);
    SoftDeleteCandidateResponse softDeleteCandidate(String id);
//    Iterable<CandidateResponse> getAllCandidates();
    List<String> getIndustryList();
    List<String> getSkillList();
}
