package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.mapper.CandidateMapper;
import com.popcorn.jrp.domain.request.candidate.CandidateSearchRequest;
import com.popcorn.jrp.domain.request.candidate.CreateCandidateDto;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateDto;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateDetailsResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateResponse;
import com.popcorn.jrp.domain.response.candidate.SoftDeleteCandidateResponse;
import com.popcorn.jrp.exception.BadRequestException;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.repository.spec.CandidateSpecification;
import com.popcorn.jrp.service.CandidateService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Transactional
public class CandidateServiceV1 implements CandidateService {

    CandidateRepository candidateRepository;
    @Qualifier("candidateMapper")
    CandidateMapper mapper;
    CandidateSpecification candidateSpecification;


    @Override
    public ApiPageResponse<CandidateResponse> getCandidates(CandidateSearchRequest request, Pageable pageable) {
        Specification<CandidateEntity> spec = candidateSpecification.getPublicSpecification(request);
        try {
            var page = candidateRepository
                    .findAll(spec, pageable);
            return mapper.toApiPageResponse(page.map(mapper::toResponse));
        } catch (Exception e) {
            throw new BadRequestException("Page request error: " + e.getMessage());
        }
    }

    @Override
    public CandidateDetailsResponse getCandidateById(Long id) {
        var found = candidateRepository.findById(id).orElseThrow(() -> new NotFoundException("Candidate"));
        return mapper.toDetailsResponse(found);
    }

    @Override
    public CandidateDetailsResponse getCandidateByUserId(Long userId) {
        var found = candidateRepository.getCandidateByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Candidate"));
        return mapper.toDetailsResponse(found);
    }

    @Override
    public CandidateDetailsResponse createCandidate(CreateCandidateDto dto) {
        CandidateEntity candidateEntity = mapper.createEntity(dto);
        candidateRepository.save(candidateEntity);
        return mapper.toDetailsResponse(candidateEntity);
    }

    @Override
    public CandidateDetailsResponse updateCandidate(Long id, UpdateCandidateDto dto) {
        CandidateEntity candidateEntity = candidateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Candidate"));
        mapper.updateEntity(candidateEntity, dto);
        candidateRepository.save(candidateEntity);
        return mapper.toDetailsResponse(candidateEntity);
    }

    @Override
    public SoftDeleteCandidateResponse softDeleteCandidate(Long id) {
        CandidateEntity candidateEntity = candidateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Candidate"));
        candidateEntity.setStatus(false);
        candidateRepository.save(candidateEntity);
        var res = mapper.toSoftDeleteResponse(candidateEntity);
        res.setUpdatedAt(LocalDateTime.now().toString());
        return res;
    }

    @Override
    public void deleteCandidate(Long id) {
        CandidateEntity candidateEntity = candidateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Candidate"));
        candidateRepository.delete(candidateEntity);
    }

    @Override
    @Cacheable("industryList") // just for demo, cache without TTL
    // @CacheEvict(cacheNames = "industryList") //delete cache
    public List<String> getIndustryList() {
        return candidateRepository.findAll().stream()
                .map(CandidateEntity::getIndustry)
                .distinct()
                .toList();
    }

    @Override
    @Cacheable("skillList") // just for demo, cache without TTL
    public List<String> getSkillList() {
        return candidateRepository.findAll().stream()
                .flatMap(c -> c.getSkills().stream())
                .distinct()
                .toList();
    }
}