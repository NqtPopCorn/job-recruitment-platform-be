package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.mapper.CandidateMapper;
import com.popcorn.jrp.domain.request.candidate.CandidateSearchAdminRequest;
import com.popcorn.jrp.domain.response.candidate.CandidateStatusStatisticResponse;
import com.popcorn.jrp.domain.response.ApiNoDataResponse;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateDetailsAdminResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateListAdminResponse;
import com.popcorn.jrp.repository.CandidateAdminRepository;
import com.popcorn.jrp.repository.spec.CandidateSpecification;
import com.popcorn.jrp.service.CandidateAdminService;
import com.popcorn.jrp.exception.BadRequestException;
import com.popcorn.jrp.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CandidateAdminServiceV1 implements CandidateAdminService {
    CandidateAdminRepository candidateRepository;
    @Qualifier("candidateMapperImpl")
    CandidateMapper mapper;
    CandidateSpecification candidateSpecification;

    @Override
    public ApiPageResponse<CandidateListAdminResponse> getCandidatesListAdmin(CandidateSearchAdminRequest request, Pageable pageable) {
        try {
            Specification<CandidateEntity> spec = candidateSpecification.getAdminSpecification(request);
            var page = candidateRepository.findAll(spec, pageable);
            return mapper.toApiPageResponse(page.map(mapper::toListAdminResponse));
        } catch (Exception e) {
            throw new BadRequestException("Page request error: " + e.getMessage());
        }
    }

    @Override
    public CandidateDetailsAdminResponse getCandidateDetailsById(Long id) {
        CandidateEntity candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ứng viên với ID: " + id));

        return mapper.toDetailsAdminResponse(candidate);
    }

    @Override
    public ApiNoDataResponse lockCandidate(Long id) {
        CandidateEntity candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ứng viên với ID: " + id));

        candidate.setStatus(!candidate.isStatus());
        candidateRepository.save(candidate);

        String message = candidate.isStatus()
                ? "Mở khóa hồ sơ ứng viên thành công!"
                : "Khóa hồ sơ ứng viên thành công!";

        return ApiNoDataResponse.builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .build();
    }

    @Override
    public CandidateStatusStatisticResponse getCandidateStatusStatistic() {
        long total = candidateRepository.count();
        long activeCount = candidateRepository.countByStatus(true);
        long lockedCount = candidateRepository.countByStatus(false);

        return CandidateStatusStatisticResponse.builder()
                .total(total)
                .activeCount(activeCount)
                .lockedCount(lockedCount)
                .build();
    }
}
