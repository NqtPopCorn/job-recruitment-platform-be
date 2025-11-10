package com.popcorn.jrp.service.impl;

import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.mapper.EmployerMapper;
import com.popcorn.jrp.domain.request.employer.EmployerQueryParameters;
import com.popcorn.jrp.domain.request.employer.UpdateEmployerDto;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.common.IndustryLabelValueDto;
import com.popcorn.jrp.domain.response.employer.*;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.EmployerRepository;
import com.popcorn.jrp.repository.spec.EmployerSpecification;
import com.popcorn.jrp.service.EmployerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        entity.setDeleted(true);
        entity.setDeletedAt(LocalDateTime.now()); // Cập nhật thủ công vì @PreUpdate có thể không chạy

        EmployerEntity savedEntity = employerRepository.save(entity);

        // Map sang DTO
        return employerMapper.toSoftDeleteDto(savedEntity);
    }
}
