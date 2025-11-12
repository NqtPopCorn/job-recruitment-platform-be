package com.popcorn.jrp.service;

import com.popcorn.jrp.domain.request.candidate.CandidateSearchAdminRequest;
import com.popcorn.jrp.domain.response.candidate.CandidateStatusStatisticResponse;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateDetailsAdminResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateListAdminResponse;
import org.springframework.data.domain.Pageable;

public interface CandidateAdminService {
    ApiPageResponse<CandidateListAdminResponse> getCandidatesListAdmin(CandidateSearchAdminRequest request, Pageable pageable);

    CandidateDetailsAdminResponse getCandidateDetailsById(Long id);

    ApiNoDataResponse lockCandidate(Long id);

    CandidateStatusStatisticResponse getCandidateStatusStatistic();
}
