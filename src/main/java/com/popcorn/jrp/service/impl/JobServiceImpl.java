package com.popcorn.jrp.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.popcorn.jrp.domain.entity.EmployerEntity;
import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.domain.mapper.JobMapper;
import com.popcorn.jrp.domain.request.job.CreateJobDto;
import com.popcorn.jrp.domain.request.job.JobQueryParameters;
import com.popcorn.jrp.domain.request.job.RelatedJobQueryParameters;
import com.popcorn.jrp.domain.request.job.UpdateJobDto;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.job.JobDashboardDto;
import com.popcorn.jrp.domain.response.job.JobDetailDto;
import com.popcorn.jrp.domain.response.job.SalaryDto;
import com.popcorn.jrp.exception.NotFoundException;
import com.popcorn.jrp.repository.EmployerRepository;
import com.popcorn.jrp.repository.JobRepository;
// import com.popcorn.jrp.repository.ApplicationRepository; // Cần có để đếm
import com.popcorn.jrp.service.JobService;
import com.popcorn.jrp.repository.spec.JobSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final EmployerRepository employerRepository;
    // private final ApplicationRepository applicationRepository; // Dùng cho Endpoint 10
    private final JobMapper jobMapper;
    private final JobSpecification jobSpecification;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Dùng cho xử lý JSON

    @Override
    @Transactional(readOnly = true)
    public ApiPageResponse<JobDetailDto> getJobsPaginated(JobQueryParameters queryParams, Pageable pageable) {
        Specification<JobEntity> spec = jobSpecification.filterBy(queryParams);
        Page<JobEntity> entityPage = jobRepository.findAll(spec, pageable);

        // Chuyển Page<Entity> sang Page<DTO>
        Page<JobDetailDto> dtoPage = entityPage.map(jobMapper::toDetailDto);

        // Dùng PageMapper để tạo response
        return jobMapper.toApiPageResponse(dtoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobDetailDto> getAllJobs() {
        List<JobEntity> entities = jobRepository.findAll(); // @Where sẽ lọc isDeleted=false
        return jobMapper.toDetailDtoList(entities);
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
    public List<String> getCompanyIndustryList(Long companyId) {
        return jobRepository.findDistinctIndustriesByEmployerId(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getSkillList() {
        List<String> allSkillsJson = jobRepository.findAllSkillsJson();
        Set<String> uniqueSkills = new HashSet<>();

        for (String json : allSkillsJson) {
            try {
                List<String> skills = objectMapper.readValue(json, new TypeReference<List<String>>() {});
                uniqueSkills.addAll(skills);
            } catch (IOException e) {
                // Bỏ qua nếu JSON không hợp lệ
            }
        }
        return new ArrayList<>(uniqueSkills);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCityList() {
        return jobRepository.findDistinctCities();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getMaxSalary() {
        List<String> allSalariesJson = jobRepository.findAllSalariesJson();
        double maxSalary = 0.0;

        for (String json : allSalariesJson) {
            try {
                SalaryDto salary = objectMapper.readValue(json, SalaryDto.class);
                if (salary != null && salary.getMax() != null && "VND".equals(salary.getCurrency())) {
                    if (salary.getMax() > maxSalary) {
                        maxSalary = salary.getMax();
                    }
                }
            } catch (IOException e) {
                // Bỏ qua
            }
        }
        return maxSalary;
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

        return jobMapper.toDetailDtoList(relatedJobs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobDashboardDto> getJobsForDashboard(Long companyId) {
        List<JobEntity> jobs = jobRepository.findByEmployerIdAndIsDeletedFalse(companyId);
        List<JobDashboardDto> dtos = jobMapper.toDashboardDtoList(jobs);

        // TODO: Cần inject ApplicationRepository để đếm số lượng hồ sơ
        // for (JobDashboardDto dto : dtos) {
        //     Long jobId = Long.parseLong(dto.getId());
        //     int count = applicationRepository.countByJobId(jobId);
        //     dto.setApplications(count);
        // }

        return dtos;
    }

    @Override
    @Transactional
    public JobDetailDto createJob(CreateJobDto createDto) {
        Long employerId;
        try {
            employerId = Long.parseLong(createDto.getEmployerId());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Employer ID không hợp lệ: " + createDto.getEmployerId());
        }

        EmployerEntity employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer với ID: " + employerId));

        JobEntity entity = jobMapper.toEntity(createDto);
        entity.setEmployer(employer);
        entity.setStatus(true); // Mặc định active
        entity.setIsDeleted(false); // Mặc định không xóa

        JobEntity savedEntity = jobRepository.save(entity);
        return jobMapper.toDetailDto(savedEntity);
    }

    @Override
    @Transactional
    public JobDetailDto updateJob(Long id, UpdateJobDto updateDto) {
        JobEntity entity = jobRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job với ID: " + id));

        jobMapper.updateEntityFromDto(updateDto, entity);

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