package com.popcorn.jrp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.domain.mapper.JobMapper;
import com.popcorn.jrp.domain.query.JobQuery;
import com.popcorn.jrp.domain.response.ApiPageResponse;
import com.popcorn.jrp.domain.response.job.JobResponse;
import com.popcorn.jrp.service.JobService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    private final JobMapper jobMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiPageResponse<JobResponse> getPaginatedJobsForCandidate(Pageable pageable,
            JobQuery jobQuery) {
        Page<JobEntity> page = jobService.getListPagination(pageable, jobQuery);
        Page<JobResponse> pageMapper = jobMapper.toPageResponse(page);
        return jobMapper.toApiPageResponse(pageMapper);
    }
}
