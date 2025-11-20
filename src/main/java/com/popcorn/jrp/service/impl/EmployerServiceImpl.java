package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.mapper.CandidateMapper;
import com.popcorn.jrp.domain.mapper.EmployerMapper;
import com.popcorn.jrp.domain.request.employer.EmployerQueryParameters;
import com.popcorn.jrp.domain.request.employer.UpdateEmployerDto;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.candidate.CandidateResponse;
import com.popcorn.jrp.domain.response.common.IndustryLabelValueDto;
import com.popcorn.jrp.domain.response.employer.*;
import com.popcorn.jrp.exception.BadRequestException;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.repository.EmployerRepository;
import com.popcorn.jrp.repository.spec.CandidateSpecification;
import com.popcorn.jrp.repository.spec.EmployerSpecification;
import com.popcorn.jrp.service.EmployerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Tự động inject các dependency 'final'
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    // private final UserRepository userRepository; // Cần nếu check user tồn tại
    // private final JobRepository jobRepository; // Cần cho getRelatedJobs
    private final EmployerMapper employerMapper;
    private final EmployerSpecification employerSpecification;
    // private final JobMapper jobMapper; // Cần cho getRelatedJobs
    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;

    @Override
    @Transactional(readOnly = true)
    public ApiPageResponse<EmployerPaginationDto> getEmployersPaginated(EmployerQueryParameters queryParams,
            Pageable pageable) {

        // 1. Tạo Specification từ query params
        Specification<EmployerEntity> spec = employerSpecification.filterBy(queryParams);

        // 2. Gọi repository
        Page<EmployerEntity> entityPage = employerRepository.findAll(spec, pageable);

        // 3. Map sang DTO
        // TODO: Cần logic tính 'jobNumber' cho mỗi DTO
        return employerMapper.toApiPageResponse(entityPage.map(employerMapper::toPaginationDto));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployerDetailDto getEmployerDetailsById(Long id) {
        EmployerEntity entity = employerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Công ty với ID: " + id));
        return employerMapper.toDetailDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployerDetailDto getEmployerDetailsByUserId(Long userId) {
        EmployerEntity entity = employerRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Công ty với User ID: " + userId));
        return employerMapper.toDetailDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RelatedJobDto> getRelatedJobsByEmployerId(Long employerId) {
        // Yêu cầu JobRepository và JobMapper
        // List<JobEntity> jobs = jobRepository.findByEmployerId(employerId);
        // return jobMapper.toRelatedJobDtoList(jobs);

        // --- Trả về rỗng (Tạm thời) ---
        if (employerId == null) {
            throw new IllegalArgumentException("Employer ID không được rỗng");
        }
        return new ArrayList<>();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IndustryLabelValueDto> getIndustryList() {
        List<String> industries = employerRepository.findDistinctIndustries();
        return employerMapper.toIndustryLabelValueDtoList(industries);
    }

    @Override
    @Transactional
    public EmployerDetailDto updateEmployer(Long id, UpdateEmployerDto updateDto) {
        EmployerEntity existingEntity = employerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Công ty với ID: " + id));

        // Dùng mapper để cập nhật các trường non-null từ DTO
        employerMapper.updateEntityFromDto(updateDto, existingEntity);

        EmployerEntity updatedEntity = employerRepository.save(existingEntity);
        return employerMapper.toDetailDto(updatedEntity);
    }

    @Override
    @Transactional
    public EmployerSoftDeleteDto softDeleteEmployer(Long id) {
        EmployerEntity entity = employerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Công ty với ID: " + id));

        entity.setStatus(false);
        entity.setIsDeleted(true);
        entity.setDeletedAt(LocalDateTime.now()); // Cập nhật thủ công vì @PreUpdate có thể không chạy

        EmployerEntity savedEntity = employerRepository.save(entity);

        // Map sang DTO
        return employerMapper.toSoftDeleteDto(savedEntity);
    }

    @Override
    @Transactional
    public boolean togglePotentialCandidate(Long employerId, Long potentialCandidateId) {
        EmployerEntity employer = this.employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer with id " + employerId));
        CandidateEntity potentialCandidate = this.candidateRepository.findById(potentialCandidateId)
                .orElseThrow(() -> new NotFoundException("Potential candidate with id " + potentialCandidateId));

        // Khởi tạo danh sách nếu null
        if (employer.getPotentialCandidates() == null) {
            employer.setPotentialCandidates(new ArrayList<>());
        }

        boolean added;
        if (employer.getPotentialCandidates().contains(potentialCandidate)) {
            // Đã tồn tại → xóa (toggle)
            employer.getPotentialCandidates().remove(potentialCandidate);
            added = false;
        } else {
            // Chưa có → thêm
            employer.getPotentialCandidates().add(potentialCandidate);
            added = true;
        }

        // Lưu lại employer để cập nhật bảng trung gian
        employerRepository.save(employer);

        return added; // trả về trạng thái hiện tại (true = added, false = removed)
    }

    @Override
    public boolean checkPotentialCandidate(Long employerId, Long potentialCandidateId) {
        EmployerEntity employer = this.employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer with id " + employerId));

        CandidateEntity candidate = this.candidateRepository.findById(potentialCandidateId)
                .orElseThrow(() -> new NotFoundException("Candidate with id " + potentialCandidateId));

        // Nếu danh sách ứng viên tiềm năng null → chưa lưu ai
        if (employer.getPotentialCandidates() == null) {
            return false;
        }

        // Kiểm tra xem danh sách có chứa candidate không
        return employer.getPotentialCandidates().contains(candidate);
    }

    @Override
    public ApiPageResponse<CandidateResponse> getPotentialCanddiatesPaginated(Long employerId, String search,
            Pageable pageable) {

        String keyword = (search == null || search.trim().isEmpty()) ? "" : search.trim();

        Page<CandidateEntity> candidatePage = employerRepository
                .findPotentialCandidatesByEmployerId(employerId, keyword, pageable);

        List<CandidateResponse> candidateResponses = candidatePage.getContent().stream()
                .map(candidateMapper::toResponse) // dùng mapper convert Entity → DTO
                .toList();

        return ApiPageResponse.<CandidateResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get potential candidates successfully")
                .results(candidateResponses)
                .meta(ApiPageResponse.Meta.builder().currentPage(candidatePage.getNumber())
                        .pageSize(candidatePage.getSize())
                        .totalPages(candidatePage.getTotalPages())
                        .totalItems(candidatePage
                                .getTotalElements())
                        .build())
                .build();
    }

}
