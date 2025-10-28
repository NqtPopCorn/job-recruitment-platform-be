package com.popcorn.jrp.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.domain.query.JobQuery;
import com.popcorn.jrp.repository.JobRepository;
import com.popcorn.jrp.repository.spec.JobSpecification;
import com.popcorn.jrp.service.JobService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Override
    public Page<JobEntity> getListPagination(Pageable pageable, JobQuery jobQuery) {
        Specification<JobEntity> spec = JobSpecification.filter(jobQuery);
        return jobRepository.findAll(spec, pageable);
    }

}
