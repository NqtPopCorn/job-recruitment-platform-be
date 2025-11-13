package com.popcorn.jrp.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import com.popcorn.jrp.domain.entity.ApplicationEntity;
import com.popcorn.jrp.domain.entity.CandidateEntity;
import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.domain.entity.ResumeEntity;
import com.popcorn.jrp.domain.mapper.ApplicationMapper;
import com.popcorn.jrp.domain.mapper.CandidateMapper;
import com.popcorn.jrp.domain.query.ApplicationQueryDto;
import com.popcorn.jrp.domain.request.application.CreateApplicationRequestDto;
import com.popcorn.jrp.domain.response.application.ApplicantResponseDto;
import com.popcorn.jrp.domain.response.application.ApplicationPageResponseDto;
import com.popcorn.jrp.domain.response.application.ApplicationResponseDto;
import com.popcorn.jrp.domain.response.candidate.CandidateResponse;
import com.popcorn.jrp.exception.BadRequestException;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.ApplicationRepository;
import com.popcorn.jrp.repository.CandidateRepository;
import com.popcorn.jrp.repository.JobRepository;
import com.popcorn.jrp.repository.ResumeRepository;
import com.popcorn.jrp.repository.spec.ApplicationSpecification;
import com.popcorn.jrp.service.ApplicationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final CandidateRepository candidateRepository;

    private final JobRepository jobRepository;

    private final ResumeRepository resumeRepository;

    private final ApplicationRepository applicationRepository;

    private final ApplicationMapper applicationMapper;

    private final CandidateMapper candidateMapper;

    private final ApplicationSpecification applicationSpecification;

    @Override
    @Transactional
    public ApplicationResponseDto createApplication(CreateApplicationRequestDto dto) {
        log.info("Start creating application: candidateId={}, jobId={}, resumeId={}",
                dto.getCandidateId(), dto.getJobId(), dto.getResumeId());

        log.debug("Input data details: {}", dto);

        // Check candidate
        CandidateEntity candidate = candidateRepository.findById(dto.getCandidateId())
                .orElseThrow(() -> {
                    log.error("Candidate not found with id={}", dto.getCandidateId());
                    return new RuntimeException("Candidate not found with id " + dto.getCandidateId());
                });
        log.info("Candidate found: {}", candidate.getId());

        // Check job
        JobEntity job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> {
                    log.error("Job not found with id={}", dto.getJobId());
                    return new RuntimeException("Job not found with id " + dto.getJobId());
                });
        log.info("Job found: {}", job.getId());

        // Check resume
        ResumeEntity resume = resumeRepository.findById(dto.getResumeId())
                .orElseThrow(() -> {
                    log.error("Resume not found with id={}", dto.getResumeId());
                    return new RuntimeException("Resume not found with id " + dto.getResumeId());
                });
        log.info("Resume found: {}", resume.getFileName());

        // Kiểm tra xem candidate đã nộp cho job này chưa
        ApplicationEntity application = applicationRepository
                .findByCandidateIdAndJobId(dto.getCandidateId(), dto.getJobId())
                .orElse(null);

        Boolean isNewApplication = (application == null);
        if (isNewApplication) {
            log.info("No existing application found, creating new one");
            application = ApplicationEntity.builder()
                    .candidate(candidate)
                    .job(job)
                    .status(ApplicationEntity.Status.PENDING)
                    .appliedAt(LocalDateTime.now())
                    .build();
        } else {
            log.info("Existing application found with id={}, will update resume and cover letter", application.getId());
        }

        // Copy resume file to applications folder, delete old file if exists
        Path source = Paths.get("images/resumes", resume.getFileName());
        Path destination = Paths.get("images/applications", resume.getFileName());
        Path destinationDir = destination.getParent();

        try {
            // Tạo thư mục đích nếu chưa tồn tại
            if (!Files.exists(destinationDir)) {
                Files.createDirectories(destinationDir);
            }

            // Xóa file cũ nếu đã tồn tại
            if (!isNewApplication && application.getFilename() != null) {
                Path destinationExist = Paths.get("images/applications", application.getFilename());
                if (Files.exists(destinationExist)) {
                    Files.delete(destinationExist);
                    log.info("Old resume file deleted: {}", destinationExist);
                }
            }

            // Copy file mới
            if (!Files.exists(source)) {
                throw new RuntimeException("Resume file not found: " + source);
            }
            FileCopyUtils.copy(source.toFile(), destination.toFile());
            log.info("Resume copied successfully from {} to {}", source, destination);

        } catch (IOException e) {
            log.error("Failed to copy resume: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to copy resume file", e);
        }

        // Cập nhật resume filename và cover letter
        application.setFilename(resume.getFileName());
        application.setCoverLetter(dto.getCoverLetter() != null ? dto.getCoverLetter() : "");

        // Lưu ứng dụng (mới hoặc cập nhật)
        ApplicationEntity savedApplication = applicationRepository.save(application);
        log.info("Application saved successfully with id={}", savedApplication.getId());

        return applicationMapper.toApplicationResponseDto(savedApplication);
    }

    @Override
    public boolean hasCandidateApplied(Long candidateId, Long jobId) {
        if (candidateId == null || jobId == null) {
            throw new IllegalArgumentException("candidateId hoặc jobId không được null");
        }
        return applicationRepository.existsByCandidateIdAndJobId(candidateId, jobId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationPageResponseDto> getApplicationsByCandidateId(Long candidateId, Pageable pageable,
            ApplicationQueryDto queryDto) {
        // Nếu pageable null hoặc không có sort, set mặc định
        if (pageable == null) {
            pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        } else if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by("createdAt").descending());
        }

        Specification<ApplicationEntity> spec = applicationSpecification.filterApplications(queryDto);
        Page<ApplicationEntity> page = applicationRepository.findAll(spec, pageable);
        Page<ApplicationPageResponseDto> pageDto = page.map(applicationMapper::toApplicationPageResponseDto);
        return pageDto;
    }

    @Override
    public List<ApplicantResponseDto> getApplicantsAppliedByJobId(Long jobId) {
        log.info("Start to getApplicantsAppliedByJobId with id {}", jobId);
        JobEntity jobIsExist = jobRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("Job with id " + jobId));
        List<ApplicationEntity> applications = applicationRepository.findAllByJob(jobIsExist);
        List<ApplicantResponseDto> candidateDtos = applications.stream()
                .map(applicationMapper::toApplicantResponseDto)
                .collect(Collectors.toList());
        return candidateDtos;
    }

    @Override
    @Transactional
    public void updateApplicantStatus(Long applicationId, String status) {
        log.info("[updateApplicantStatus] Request to update aapplicationId={} → '{}'",
                applicationId, status);

        // Tìm application theo job và applicant
        ApplicationEntity application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() -> {
                    log.error("Application not found forapplicationId={}", applicationId);
                    return new NotFoundException(
                            String.format("Application not found for applicationId %d", applicationId));
                });

        ApplicationEntity.Status currentStatus = application.getStatus();
        ApplicationEntity.Status newStatusEnum;

        try {
            newStatusEnum = ApplicationEntity.Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid status '{}' provided for applicationId={}", status, applicationId);
            throw new BadRequestException("Invalid application status: " + status);
        }

        log.debug("Current status={}, Requested new status={}", currentStatus, newStatusEnum);

        // Logic cập nhật trạng thái
        switch (newStatusEnum) {
            case REVIEWED -> {
                if (currentStatus != ApplicationEntity.Status.ACCEPTED &&
                        currentStatus != ApplicationEntity.Status.REJECTED) {
                    log.info("Changing status from {} → REVIEWED for applicationId={}",
                            currentStatus, applicationId);
                    application.setStatus(newStatusEnum);
                } else {
                    log.warn("Skip update: current status is final ({}) for applicationId={}",
                            currentStatus, applicationId);
                }
            }
            case ACCEPTED, REJECTED -> {
                log.info("Updating status from {} → {} for applicationId={}",
                        currentStatus, newStatusEnum, applicationId);
                application.setStatus(newStatusEnum);
            }
            default -> {
                log.error("Unsupported status '{}' for applicationId={}", status, applicationId);
                throw new BadRequestException("Unsupported application status: " + status);
            }
        }

        // Lưu thay đổi
        applicationRepository.save(application);
        log.info("Status updated successfully for applicationId={} → {}",
                applicationId, application.getStatus());
    }

}
