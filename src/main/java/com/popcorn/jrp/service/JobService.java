package com.popcorn.jrp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.popcorn.jrp.domain.entity.JobEntity;
import com.popcorn.jrp.domain.query.JobQuery;

public interface JobService {
    public Page<JobEntity> getListPagination(Pageable pageable, JobQuery jobQuery);
}