package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.entity.NotificationEntity;
import com.popcorn.jrp.domain.mapper.CandidateMapper;
import com.popcorn.jrp.domain.mapper.NotificationMapper;
import com.popcorn.jrp.domain.request.candidate.CandidateSearchRequest;
import com.popcorn.jrp.domain.response.candidate.CandidateStatisticsResponse;
import com.popcorn.jrp.domain.request.candidate.UpdateCandidateDto;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateDetailsResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateResponse;
import com.popcorn.jrp.domain.response.candidate.JobAppliedRecentlyResponse;
import com.popcorn.jrp.domain.response.candidate.SoftDeleteCandidateResponse;
import com.popcorn.jrp.domain.response.notification.NotificationResponse;
import com.popcorn.jrp.exception.BadRequestException;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.ApplicationRepository;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.repository.ChatRepository;
import com.popcorn.jrp.repository.NotificationRepository;
import com.popcorn.jrp.repository.spec.CandidateSpecification;
import com.popcorn.jrp.service.CandidateService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
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
    ApplicationRepository applicationRepository;
    NotificationRepository notificationRepository;
    ChatRepository chatRepository;
    NotificationMapper notificationMapper;

    @Qualifier("candidateMapperImpl")
    CandidateMapper mapper;
    CandidateSpecification candidateSpecification;

    @Override
    public ApiPageResponse<CandidateResponse> getCandidates(CandidateSearchRequest request, Pageable pageable) {
        Specification<CandidateEntity> spec = candidateSpecification.getPublicSpecification(request);
        try {
            var page = candidateRepository.findAll(spec, pageable);
            return mapper.toApiPageResponse(page.map(mapper::toResponse));
        } catch (Exception e) {
            throw new BadRequestException("Page request error: " + e.getMessage());
        }
    }

    @Override
    public CandidateDetailsResponse getCandidateById(Long id) {
        CandidateEntity found = candidateRepository.findByIdAndStatusTrueAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Candidate"));
        return mapper.toDetailsResponse(found);
    }

    @Override
    public CandidateDetailsResponse getCandidateByUserId(Long userId) {
        CandidateEntity found = candidateRepository.findByUserIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new NotFoundException("Candidate"));
        return mapper.toDetailsResponse(found);
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
        candidateEntity.setDeleted(true);
        candidateRepository.save(candidateEntity);
        var res = mapper.toSoftDeleteResponse(candidateEntity);
        res.setDeletedAt(LocalDateTime.now());
        return res;
    }

    @Override
    public void deleteCandidate(Long id) {
        CandidateEntity candidateEntity = candidateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Candidate"));
        candidateRepository.delete(candidateEntity);
    }

    @Override
    @Cacheable("industryList")
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

    @Override
    public CandidateStatisticsResponse getCandidateStatistics(Long candidateId) {

        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate with ID " + candidateId + " not found"));

        Long userId = candidate.getUser() != null ? candidate.getUser().getId() : null;
        if (userId == null) {
            throw new BadRequestException("Candidate does not have associated user");
        }

        Long appliedJobsCount = applicationRepository.countByCandidateId(candidateId);

        Long jobAlertsCount = notificationRepository.countByUserId(userId);

        Long messagesCount = chatRepository.countByUserId(userId);


        Long shortlistCount = 0L;

        return CandidateStatisticsResponse.builder()
                .appliedJobsCount(appliedJobsCount)
                .jobAlertsCount(jobAlertsCount)
                .messagesCount(messagesCount)
                .shortlistCount(shortlistCount)
                .build();
    }

    @Override
    public List<NotificationResponse> getLatestNotifications(Long candidateId) {

        CandidateEntity candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new NotFoundException("Candidate with ID " + candidateId + " not found"));

        Long userId = candidate.getUser() != null ? candidate.getUser().getId() : null;
        if (userId == null) {
            throw new BadRequestException("Candidate does not have associated user");
        }

        Pageable pageable = PageRequest.of(0, 6);
        List<NotificationEntity> notifications = notificationRepository.findLatestByUserId(userId, pageable);

        return notificationMapper.toResponseList(notifications);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobAppliedRecentlyResponse> getRecentlyAppliedJobs(Long candidateId, Integer limit) {
        if (!candidateRepository.existsById(candidateId)) {
            throw new BadRequestException("Candidate not found with id: " + candidateId);
        }

        int pageSize = (limit != null && limit > 0) ? limit : 6;

        List<ApplicationEntity> applications = applicationRepository
                .findRecentApplicationsByCandidateId(candidateId, PageRequest.of(0, pageSize));

        return mapper.toJobAppliedRecentlyResponseList(applications);
    }
}