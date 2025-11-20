package com.popcorn.jrp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.domain.dto.notification.JobNotificationDto;
import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.domain.mapper.JobMapper;
import com.popcorn.jrp.domain.request.job.*;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.job.JobDashboardDto;
import com.popcorn.jrp.domain.response.job.JobDetailDto;
import com.popcorn.jrp.domain.response.job.JobResponseDto;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.messaging.producer.NotificationProducer;
import com.popcorn.jrp.repository.EmployerRepository;
import com.popcorn.jrp.repository.JobRepository;
// import com.popcorn.jrp.repository.ApplicationRepository; // Cần có để đếm
import com.popcorn.jrp.service.JobService;
import com.popcorn.jrp.repository.spec.JobSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.management.Notification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final EmployerRepository employerRepository;
    private final JobMapper jobMapper;
    private final JobSpecification jobSpecification;
    private final NotificationProducer notificationProducer;

    @Override
    @Transactional(readOnly = true)
    public ApiPageResponse<JobResponseDto> getJobsPaginated(JobQueryParameters queryParams, Pageable pageable) {
        log.info("Pagination info: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        try {
            Specification<JobEntity> spec = jobSpecification.publicFilter(queryParams);
            log.debug("Specification filter created successfully.");

            Page<JobEntity> entityPage = jobRepository.findAll(spec, pageable);
            log.debug("JobEntity query executed successfully. Found {} records.", entityPage.getTotalElements());

            // Map to DTO
            Page<JobResponseDto> dtoPage = entityPage.map(jobMapper::toJobResponseDto);
            log.debug("Entity-to-DTO mapping completed.");

            ApiPageResponse<JobResponseDto> response = jobMapper.toApiPageResponse(dtoPage);
            response.setMessage("Job list retrieved successfully with pagination!");
            response.setStatusCode(HttpStatus.OK.value());

            log.info("Returning successful response. Total pages: {}, total records: {}",
                    response.getMeta().getTotalPages(), response.getMeta().getTotalItems());
            return response;

        } catch (Exception e) {
            log.error("Error while retrieving job list: {}", e.getMessage(), e);
            throw e; // rethrow to let Spring handle it as 500 Internal Server Error
        }
    }

    @Override
    @Transactional(readOnly = true)
    public JobDetailDto getJobById(Long id) {
        JobEntity entity = jobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job với ID: " + id));
        return jobMapper.toDetailDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getJobIndustryList() {
        return jobRepository.findDistinctIndustries();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCompanyIndustryListByCompanyId(Long companyId) {
        return jobRepository.findDistinctIndustriesByEmployerId(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getSkillList() {
        return jobRepository.findAll().stream()
                .flatMap(c -> c.getSkills().stream())
                .distinct()
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCityList() {
        return jobRepository.findDistinctCities();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getMaxSalaryWithCurrency(String currency) {
        return jobRepository.getMaxSalaryWithCurrency(currency);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobDetailDto> getRelatedJobs(Long id, RelatedJobQueryParameters queryParams) {
        JobEntity currentJob = jobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job với ID: " + id));

        String industry = (queryParams.getIndustry() != null) ? queryParams.getIndustry() : currentJob.getIndustry();
        String country = (queryParams.getCountry() != null) ? queryParams.getCountry() : currentJob.getCountry();
        String city = (queryParams.getCity() != null) ? queryParams.getCity() : currentJob.getCity();

        List<JobEntity> relatedJobs = jobRepository
                .findByIdNotAndIndustryAndCountryAndCityAndIsDeletedFalse(id, industry, country, city);
        List<JobDetailDto> relatedJobDtos = relatedJobs.stream()
                .map(jobMapper::toDetailDto)
                .collect(Collectors.toList());
        return relatedJobDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public ApiPageResponse<JobDashboardDto> getJobsForDashboard(Long companyId, Pageable pageable,
            EmployerJobQueryDto queryParams) {
        employerRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company with ID: " + companyId));
        Page<JobEntity> jobs = jobRepository.findAll(
                jobSpecification.dashboardFilter(queryParams),
                pageable);
        return jobMapper.toApiPageResponse(jobs.map(jobMapper::toDashboardDto));
    }

    @Override
    @Transactional
    public JobDetailDto createJob(CreateJobDto createDto) {
        Long employerId;
        try {
            employerId = Long.parseLong(createDto.getCompanyId());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Employer ID không hợp lệ: " + createDto.getCompanyId());
        }

        EmployerEntity employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer với ID: " + employerId));

        // Check employerServicePackage

        // Create new Job
        JobEntity entity = jobMapper.toEntity(createDto);
        entity.setEmployer(employer);
        entity.setStatus(true); // Mặc định active
        entity.setIsDeleted(false); // Mặc định không xóa

        JobEntity savedEntity = jobRepository.save(entity);

        // Send notification about new job creation
        notificationProducer.sendJobCreatedCreatedNotification(savedEntity);

        return jobMapper.toDetailDto(savedEntity);
    }

    @Override
    @Transactional
    public JobDetailDto updatePartialJob(Long id, UpdateJobDto updateDto) {
        JobEntity entity = jobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job với ID: " + id));

        jobMapper.updatePartialEntityFromDto(updateDto, entity);

        JobEntity updatedEntity = jobRepository.save(entity);
        return jobMapper.toDetailDto(updatedEntity);
    }

    @Override
    @Transactional
    public JobDetailDto updateJob(Long id, UpdateJobDto updateDto) {
        JobEntity entity = jobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job với ID: " + id));

        // Cập nhật các trường từ DTO sang entity hiện có
        jobMapper.updateEntityFromDto(updateDto, entity);

        // Lưu lại entity đã được cập nhật
        JobEntity updatedEntity = jobRepository.save(entity);

        return jobMapper.toDetailDto(updatedEntity);
    }

    @Override
    @Transactional
    public void softDeleteJob(Long id) {
        JobEntity entity = jobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job với ID: " + id));

        entity.setIsDeleted(true);
        entity.setStatus(false); // Khi xóa mềm cũng nên tắt active
        jobRepository.save(entity);
    }
}
