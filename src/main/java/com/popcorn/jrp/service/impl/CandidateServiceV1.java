package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.mapper.CandidateMapper;
import com.popcorn.jrp.domain.request.CandidateSearchRequest;
import com.popcorn.jrp.domain.request.CreateCandidateDto;
import com.popcorn.jrp.domain.request.UpdateCandidateDto;
import com.popcorn.jrp.domain.response.CandidateDetailsResponse;
import com.popcorn.jrp.domain.response.CandidateResponse;
import com.popcorn.jrp.domain.response.SoftDeleteCandidateResponse;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.repository.spec.CandidateSpecification;
import com.popcorn.jrp.service.CandidateService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CandidateServiceV1 implements CandidateService {

    CandidateRepository candidateRepository;
    CandidateMapper mapper;

    @Override
    public Page<CandidateResponse> getCandidates(CandidateSearchRequest request, Pageable pageable) {
        Specification<CandidateEntity> spec = CandidateSpecification.getSpecification(request);
        return candidateRepository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Override
    public CandidateDetailsResponse getCandidateById(String id) {
        return null;
    }

    @Override
    public CandidateDetailsResponse getCandidateByUserId(String userId) {
        return null;
    }

    @Override
    public CandidateDetailsResponse createCandidate(CreateCandidateDto dto) {
        return null;
    }

    @Override
    public CandidateDetailsResponse updateCandidate(String id, UpdateCandidateDto dto) {
        return null;
    }

    @Override
    public SoftDeleteCandidateResponse softDeleteCandidate(String id) {
        return null;
    }

    @Override
    public List<String> getIndustryList() {
        return List.of();
    }

    @Override
    public List<String> getSkillList() {
        return List.of();
    }
}
